package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.network

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.grpc.client.GrpcClient

private val grpcClient = GrpcClient(
    scheme = "http",
    domain = "0.0.0.0",
    port = 5400,
)

internal actual suspend fun getRecommendedArticleIds(customerId: String): List<String> {
    return grpcClient.getRecommendedArticleIds(customerId)
}