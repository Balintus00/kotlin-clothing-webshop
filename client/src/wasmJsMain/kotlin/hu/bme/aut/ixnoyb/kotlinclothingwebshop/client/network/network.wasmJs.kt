package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.network

internal actual suspend fun getRecommendedArticleIds(customerId: String): List<String> {
    return listOf("1") // TODO
}