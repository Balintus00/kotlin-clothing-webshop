package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.grpc

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.ClothingWebshopGrpcKt
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.IdRequest
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.ListIdResponse
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.listIdResponse
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.SqlClient
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider
import org.jetbrains.kotlinx.dl.onnx.inference.inferAndCloseUsing
import org.slf4j.LoggerFactory

class ClothingWebshopGrpcService(
    private val sqlClient: SqlClient,
) : ClothingWebshopGrpcKt.ClothingWebshopCoroutineImplBase() {

    override suspend fun getRecommendedArticleIds(request: IdRequest): ListIdResponse {
        try {
            logger.info("getRecommendedArticleIds called with ${request.id}")

            val model = OnnxInferenceModel.load(QUERY_MODEL_PATH)
            val queryVector = model.inferAndCloseUsing(ExecutionProvider.CPU()) { inferenceModel ->
                val inputSize = model.inputDimensions.last().toInt()
                logger.debug("Model input size: $inputSize")
                logger.debug("Model output shape: {}", model.outputShape.toSet())

                // TODO read customer data from data and create proper input tensor
                val input = FloatArray(inputSize)
                for (i in 0 until inputSize) {
                    input[i] = i.toFloat()
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

            val recommendedArticleIds = sqlClient
                .query(sqlQuery)
                .execute()
                .await()

            val result = recommendedArticleIds.map { "${it.getInteger("articles_id")}" }
            logger.info("ANN search result: $result")

            return listIdResponse {
                ids.addAll(result.toSet())
            }
        } catch (t: Throwable) {
            logger.error("getRecommendedArticleIds operation failed!", t)
            throw t
        }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(ClothingWebshopGrpcService::class.java)

        private const val QUERY_MODEL_PATH = "src/main/resources/retrieval_query_tower_model.onnx"
    }
}