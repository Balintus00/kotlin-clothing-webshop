package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.AuthenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification

internal interface ArticleRemoteDatasource {

    suspend fun getArticlePreviews(
        filter: ArticleFilter,
        pageSpecification: ArticlePageSpecification,
    ): List<ArticlePreview>

    suspend fun getByID(articleID: ArticleID): Article

    suspend fun getRecommendedArticlePreviews(
        authenticationToken: AuthenticationToken?,
    ): List<ArticlePreview>

    companion object {

        const val ERROR_MESSAGE_NOT_FOUND = "ERROR_MESSAGE_NOT_FOUND"
    }
}