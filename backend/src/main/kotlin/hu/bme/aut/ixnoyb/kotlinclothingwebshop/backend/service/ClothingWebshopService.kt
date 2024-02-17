package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.service

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider
import org.jetbrains.kotlinx.dl.onnx.inference.inferAndCloseUsing
import org.slf4j.LoggerFactory
import java.nio.file.Paths
import kotlin.math.pow

class ClothingWebshopService {

    private val databaseConnectionFactory = ConnectionFactories.get(
        ConnectionFactoryOptions.builder()
            .option(DRIVER, "postgresql")
            .option(HOST, "localhost")
            .option(PORT, 5432)
            .option(USER, "postgres")
            .option(PASSWORD, "password")
            .build()
    )

    suspend fun getRecommendedArticleIds(customerId: String): List<String> {
        var databaseConnection: Connection? = null
        try {
            logger.info("getRecommendedArticleIds called with $customerId")

            val model = OnnxInferenceModel.load(QUERY_MODEL_PATH)
            val queryVector = model.inferAndCloseUsing(ExecutionProvider.CPU()) { inferenceModel ->
                val inputSize = model.inputDimensions.last().toInt()
                logger.debug("Model input size: $inputSize")
                logger.debug("Model output shape: {}", model.outputShape.toSet())

                val input = FloatArray(inputSize)
                for (i in 0 until inputSize) {
                    input[i] = when (customerId) {
                        "customerId2" -> i * (-1f).pow(i) * 1000
                        "customerId3" -> i * (-1f).pow(i % 3) * 43
                        else -> i.toFloat()
                    }
                }

                inferenceModel.reshape(inputSize.toLong())

                inferenceModel.predictSoftly(input).toList().also {
                    logger.info("Query vector: $it")
                }
            }

            val sqlQuery = """
                    SELECT articles_id FROM articles 
                    ORDER BY recommendation_embedding <-> '$queryVector' 
                    LIMIT 5;
                    """.trimIndent()

            logger.debug("SQL query to execute:\n$sqlQuery")

            val connection = databaseConnectionFactory.create().awaitFirst().also {
                databaseConnection = it
            }
            connection.beginTransaction().awaitFirstOrNull()
            val resultIds = try {
                val query = connection.createStatement(sqlQuery)
                val invalidIdValue = "-1"
                val results = query.add().execute().awaitFirstOrNull()?.map { row, metadata ->
                    try {
                        row.get("articles_id")?.toString() ?: invalidIdValue
                    } catch (t: Throwable) {
                        logger.error(
                            "Failed to retrieve ID from result with metadata: $metadata"
                        )
                        invalidIdValue
                    }
                }?.asFlow()?.fold(mutableListOf<String>()) { acc, value ->
                    acc.apply {
                        if (value != invalidIdValue) {
                            acc.add(value)
                        }
                    }
                } ?: run {
                    logger.error("Query result object is null!")
                    emptyList()
                }
                connection.commitTransaction().awaitFirstOrNull()
                results
            } catch (t: Throwable) {
                logger.error(
                    "Database transaction failed:\n${t.message ?: ""}\n${t.stackTraceToString()}"
                )
                connection.rollbackTransaction().awaitFirstOrNull()
                emptyList()
            }

            connection.close()
            databaseConnection = null

            logger.info("Results: $resultIds")

            check(resultIds.isNotEmpty())

            return resultIds
        } catch (t: Throwable) {
            databaseConnection?.close()
            logger.error("getRecommendedArticleIds operation failed!", t)
            throw t
        }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(ClothingWebshopService::class.java)

        private val QUERY_MODEL_PATH = Paths.get(
            Paths.get("").toAbsolutePath().toString(),
            "backend/src/main/resources/retrieval_query_tower_model.onnx"
        ).toString()
    }
}