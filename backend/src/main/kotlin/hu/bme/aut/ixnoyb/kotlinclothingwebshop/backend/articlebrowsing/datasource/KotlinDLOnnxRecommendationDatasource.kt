package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.RecommendationDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import org.jetbrains.kotlinx.dl.impl.util.flattenFloats
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.slf4j.LoggerFactory
import kotlin.math.ceil

class KotlinDLOnnxRecommendationDatasource : RecommendationDatasource {

    override suspend fun getUserEmbedding(
        userIDIndex: Int,
        userBirthDate: DateOfBirth,
    ): FloatArray = getUserEmbeddingVector(
        encodedUserID = userIDIndex.encodeUserIDIndex(),
        encodedUserAge = userBirthDate.encodeUserAge(),
    )

    private suspend fun Int.encodeUserIDIndex(): FloatArray {
        val userIDEncoderModel = OnnxInferenceModel.load(
            withContext(Dispatchers.IO) {
                javaClass.getResourceAsStream(
                    "/models/retrieval_query_id_encoder.onnx"
                )!!.readAllBytes()
            }
        )

        val encoderInputSize = userIDEncoderModel.inputDimensions.last().toInt()
        userIDEncoderModel.reshape(encoderInputSize.toLong())

        logger.debug("Model input size: $encoderInputSize")
        logger.debug("Model output shape: {}", userIDEncoderModel.outputShape.toSet())

        return userIDEncoderModel.use {
            userIDEncoderModel.predictSoftly(
                FloatArray(1).apply { this[0] = this@encodeUserIDIndex.toFloat() }
            )
        }
    }

    private fun DateOfBirth.encodeUserAge(): FloatArray {
        val age = this.value.yearsUntil(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )

        val supportedAge = age.coerceIn(SUPPORTED_MINIMUM_AGE..SUPPORTED_MAXIMUM_AGE)

        val ageGroupIndex = (supportedAge - SUPPORTED_MINIMUM_AGE) / CUSTOMER_AGE_GROUP_SIZE

        return FloatArray(AGE_GROUP_COUNT).apply {
            for (i in 0..<AGE_GROUP_COUNT) {
                this[i] = if (ageGroupIndex == i) 1f else 0f
            }
        }
    }

    private suspend fun getUserEmbeddingVector(
        encodedUserID: FloatArray,
        encodedUserAge: FloatArray,
    ): FloatArray {
        val towerModel = OnnxInferenceModel.load(
            withContext(Dispatchers.IO) {
                javaClass.getResourceAsStream(
                    "/models/retrieval_query_tower_model.onnx"
                )!!.readAllBytes()
            }
        )

        logger.debug("xdd1: ${encodedUserID.size}")
        logger.debug("xdd2: ${encodedUserAge.size}")

        val input = arrayOf(encodedUserID, encodedUserAge).flattenFloats()

        val towerInputSize = towerModel.inputDimensions.last().toInt()
        towerModel.reshape(towerInputSize.toLong())

        logger.debug("Model input size: $towerInputSize")
        logger.debug("Model output shape: {}", towerModel.outputShape.toSet())

        return towerModel.use {
            towerModel.predictSoftly(input)
        }
    }

    companion object {

        @JvmStatic
        private val logger = LoggerFactory.getLogger(
            KotlinDLOnnxRecommendationDatasource::class.simpleName!!
        )

        private const val SUPPORTED_MINIMUM_AGE = 16
        private const val SUPPORTED_MAXIMUM_AGE = 99
        private const val CUSTOMER_AGE_GROUP_SIZE = 4
        private val AGE_GROUP_COUNT = ceil(
            (SUPPORTED_MAXIMUM_AGE - SUPPORTED_MINIMUM_AGE).toFloat()
                    / CUSTOMER_AGE_GROUP_SIZE.toFloat()
        ).toInt()
    }
}