package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.network

import com.apollographql.apollo3.ApolloClient
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.GetRecommendedArticlesQuery

internal expect fun getServerDomain(): String

private val apolloClient = ApolloClient.Builder()
    .serverUrl("http://${getServerDomain()}:5400/graphql")
    .build()

internal suspend fun getRecommendedArticleIds(customerId: String): List<String> {
    val response = apolloClient.query(GetRecommendedArticlesQuery(customerId)).execute()
    return response.dataAssertNoErrors.getRecommendedArticles.map { it.id }
}