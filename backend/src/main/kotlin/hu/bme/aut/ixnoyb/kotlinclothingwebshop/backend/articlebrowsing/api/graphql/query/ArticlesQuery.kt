package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.query

import com.expediagroup.graphql.server.operations.Query
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.toDomainArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.api.graphql.dto.toGraphQLArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.ArticleService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Brand
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Name
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.Price
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.PageSize
import org.slf4j.LoggerFactory

class ArticlesQuery(private val service: ArticleService) : Query {

    @Suppress("unused")
    suspend fun getArticles(
        filter: ArticleFilter,
        lastReadArticleID: String?,
        pageSize: Int
    ): List<Article> {
        return try {
            service.getArticles(
                filter = filter.toDomainArticleFilter(),
                page = ArticlePageSpecification(
                    lastReceivedArticleID = lastReadArticleID?.let { ArticleID(it) },
                    size = PageSize(pageSize),
                )
            ).map { it.toGraphQLArticle() }
        } catch (t: Throwable) {
            logger.info(
                "getArticles failed with exception $t\nMessage: ${t.message}\n" +
                        "Stacktrace: ${t.stackTraceToString()}"
            )

            when (t.message) {
                ArticleService.ERROR_MESSAGE_ARTICLE_NOT_FOUND -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
                }

                Name.ERROR_MESSAGE_EMPTY -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_NAME_EMPTY)
                }

                Name.ERROR_MESSAGE_INVALID_CHARACTER -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_NAME_INVALID_CHARACTER)
                }

                Name.ERROR_MESSAGE_TOO_LONG -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_NAME_TOO_LONG)
                }

                Name.ERROR_MESSAGE_TOO_SHORT -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_NAME_TOO_SHORT)
                }

                Brand.ERROR_MESSAGE_EMPTY -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_BRAND_EMPTY)
                }

                Brand.ERROR_MESSAGE_INVALID_CHARACTER -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_BRAND_INVALID_CHARACTER)
                }

                Brand.ERROR_MESSAGE_TOO_LONG -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_BRAND_TOO_LONG)
                }

                Brand.ERROR_MESSAGE_TOO_SHORT -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_BRAND_TOO_SHORT)
                }

                Price.ERROR_MESSAGE_TO_HIGH -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_PRICE_TO_HIGH)
                }

                Price.ERROR_MESSAGE_TOO_LOW -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_PRICE_TOO_LOW)
                }

                PageSize.ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH)
                }

                PageSize.ERROR_MESSAGE_PAGE_SIZE_TOO_LOW -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_PAGE_SIZE_TOO_LOW)
                }

                else -> throw Exception()
            }
        }
    }

    companion object {

        @JvmStatic
        private val logger = LoggerFactory.getLogger(ArticleService::class.simpleName!!)

        private const val ERROR_MESSAGE_ARTICLE_NOT_FOUND = "ERROR_MESSAGE_ARTICLE_NOT_FOUND"

        private const val ERROR_MESSAGE_NAME_EMPTY = "ERROR_MESSAGE_NAME_EMPTY"
        private const val ERROR_MESSAGE_NAME_INVALID_CHARACTER =
            "ERROR_MESSAGE_NAME_INVALID_CHARACTER"
        private const val ERROR_MESSAGE_NAME_TOO_LONG = "ERROR_MESSAGE_NAME_TOO_LONG"
        private const val ERROR_MESSAGE_NAME_TOO_SHORT = "ERROR_MESSAGE_NAME_TOO_SHORT"

        private const val ERROR_MESSAGE_BRAND_EMPTY = "ERROR_MESSAGE_BRAND_EMPTY"
        private const val ERROR_MESSAGE_BRAND_INVALID_CHARACTER =
            "ERROR_MESSAGE_BRAND_INVALID_CHARACTER"
        private const val ERROR_MESSAGE_BRAND_TOO_LONG = "ERROR_MESSAGE_BRAND_TOO_LONG"
        private const val ERROR_MESSAGE_BRAND_TOO_SHORT = "ERROR_MESSAGE_BRAND_TOO_SHORT"

        private const val ERROR_MESSAGE_PRICE_TOO_LOW = "ERROR_MESSAGE_PRICE_TOO_LOW"
        private const val ERROR_MESSAGE_PRICE_TO_HIGH = "ERROR_MESSAGE_PRICE_TO_HIGH"

        private const val ERROR_MESSAGE_PAGE_SIZE_TOO_LOW = "ERROR_MESSAGE_PAGE_SIZE_TOO_LOW"
        private const val ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH = "ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH"
    }
}