package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.query

import com.expediagroup.graphql.server.operations.Query
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.dto.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.service.ClothingWebshopService

class RecommendedArticlesQuery(private val service: ClothingWebshopService) : Query {

    @Suppress("unused")
    suspend fun getRecommendedArticles(customerId: String): List<Article> {
        return service.getRecommendedArticleIds(customerId).map { Article(it) }
    }
}