package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.network

internal expect suspend fun getRecommendedArticleIds(customerId: String): List<String>