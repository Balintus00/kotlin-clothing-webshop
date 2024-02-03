package hu.bme.aut.ixnoyb.kotlinclothingwebshop.grpc.client

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.ClothingWebshopClient
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.api.grpc.IdRequest
import okhttp3.OkHttpClient
import okhttp3.Protocol
import com.squareup.wire.GrpcClient as WireGrpcClient

@Suppress("unused") // Public API
class GrpcClient(
    scheme: String,
    domain: String,
    port: Int,
) {

    private val wireGrpcClient = WireGrpcClient.Builder()
        .client(OkHttpClient.Builder().protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE)).build())
        .baseUrl("$scheme://$domain:$port")
        .build()
        .create(ClothingWebshopClient::class)

    suspend fun getRecommendedArticleIds(customerId : String): List<String> {
        return wireGrpcClient.GetRecommendedArticleIds().execute(IdRequest(id = customerId)).ids
    }
}