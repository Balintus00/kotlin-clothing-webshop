package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService.Companion.ERROR_MESSAGE_ARTICLE_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService.Companion.ERROR_MESSAGE_UNEXPECTED_ERROR
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService.Companion.ERROR_MESSAGE_USER_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.ArticleDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource.RecommendationDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import org.slf4j.LoggerFactory

interface ArticleService {

    suspend fun getArticleByID(articleID: ArticleID): Article

    suspend fun getArticles(filter: ArticleFilter, page: ArticlePageSpecification): List<Article>

    suspend fun getRecommendedArticles(userID: UserID?): List<Article>

    companion object {

        const val ERROR_MESSAGE_ARTICLE_NOT_FOUND = "ERROR_MESSAGE_ARTICLE_NOT_FOUND"
        const val ERROR_MESSAGE_USER_NOT_FOUND = "ERROR_MESSAGE_USER_NOT_FOUND"
        const val ERROR_MESSAGE_UNEXPECTED_ERROR = "ERROR_MESSAGE_UNEXPECTED_ERROR"
    }
}

class DefaultArticleService(
    private val articleDatasource: ArticleDatasource,
    private val recommendationDatasource: RecommendationDatasource,
    private val userDatasource: UserDatasource,
) : ArticleService {

    override suspend fun getArticleByID(articleID: ArticleID): Article = try {
        articleDatasource.getArticleByID(articleID)
    } catch (t: Throwable) {
        if (t.message == ArticleDatasource.ERROR_MESSAGE_ARTICLE_NOT_FOUND) {
            throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
        } else {
            t.handleUnexpectedCatch()
        }
    }

    override suspend fun getArticles(
        filter: ArticleFilter,
        page: ArticlePageSpecification,
    ): List<Article> = try {
        articleDatasource.getArticles(filter, page)
    } catch (t: Throwable) {
        logger.info("Exception in getArticles: $t\n${t.message}\n${t.stackTraceToString()}")

        if (t.message == ArticleDatasource.ERROR_MESSAGE_ARTICLE_NOT_FOUND) {
            throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
        } else {
            t.handleUnexpectedCatch()
        }
    }

    private fun Throwable.handleUnexpectedCatch(): Nothing {
        logger.error("Unexpected exception: ${message}\n${stackTraceToString()}")
        throw IllegalStateException(ERROR_MESSAGE_UNEXPECTED_ERROR)
    }

    override suspend fun getRecommendedArticles(userID: UserID?): List<Article> = try {
        val (recommendationIndex, dateOfBirth) =
            userDatasource.getUserRecommendationIndexAndBirthDate(userID)

        articleDatasource.getRecommendedArticles(
            recommendationDatasource.getUserEmbedding(
                userBirthDate = dateOfBirth,
                userIDIndex = recommendationIndex,
            )
        )
    } catch (t: Throwable) {
        logger.info(
            "Exception in getRecommendedArticles: $t\n${t.message}\n${t.stackTraceToString()}"
        )

        if (t.message == UserDatasource.ERROR_MESSAGE_USER_NOT_FOUND) {
            throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)
        } else {
            t.handleUnexpectedCatch()
        }
    }


    companion object {

        @JvmStatic
        private val logger = LoggerFactory.getLogger(DefaultArticleService::class.simpleName!!)
    }
}