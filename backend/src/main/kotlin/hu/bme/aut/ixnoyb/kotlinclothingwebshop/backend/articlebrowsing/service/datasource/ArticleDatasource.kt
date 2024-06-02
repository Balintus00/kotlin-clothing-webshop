package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification

interface ArticleDatasource {

    suspend fun getArticleByID(articleID: ArticleID): Article

    suspend fun getArticles(filter: ArticleFilter, page: ArticlePageSpecification): List<Article>

    suspend fun getRecommendedArticles(userEmbedding: FloatArray): List<Article>

    companion object {

        const val ERROR_MESSAGE_ARTICLE_NOT_FOUND = "ERROR_MESSAGE_ARTICLE_NOT_FOUND"
    }
}