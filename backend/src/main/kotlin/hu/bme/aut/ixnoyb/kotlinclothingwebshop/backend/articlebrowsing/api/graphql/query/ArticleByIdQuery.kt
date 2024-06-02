package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query

import com.expediagroup.graphql.server.operations.Query
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.toGraphQLArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import org.slf4j.LoggerFactory

class ArticleByIdQuery(private val service: ArticleService) : Query {

    @Suppress("unused")
    suspend fun getArticleById(articleID: String): Article = try {
       service.getArticleByID(ArticleID(articleID)).toGraphQLArticle()
    } catch (t: Throwable) {
        logger.info("Exception caught: $t\n${t.message}\n${t.stackTraceToString()}")

        if (t.message == ArticleService.ERROR_MESSAGE_ARTICLE_NOT_FOUND) {
            throw IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND)
        } else {
            logger.warn("Unexpected exception: $t\n${t.message}\n${t.stackTraceToString()}")

            throw Exception()
        }
    }

    companion object {

        private const val ERROR_MESSAGE_NOT_FOUND = "ERROR_MESSAGE_NOT_FOUND"

        @JvmStatic
        private val logger = LoggerFactory.getLogger(ArticleByIdQuery::class.simpleName!!)
    }
}