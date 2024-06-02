package hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlin.jvm.JvmInline

@Suppress("unused")
data class ArticlePageSpecification(
    val lastReceivedArticleID: ArticleID? = null,
    val size: PageSize,
)

@JvmInline
@Suppress("unused")
value class PageSize(val value: Int) {

    init {
        require(value > 0) { ERROR_MESSAGE_PAGE_SIZE_TOO_LOW }
        require(value <= MAXIMUM_PAGE_SIZE) { ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH }
    }

    companion object {

        private const val MAXIMUM_PAGE_SIZE = 128

        const val ERROR_MESSAGE_PAGE_SIZE_TOO_LOW = "Page size must be greater than 0!"
        const val ERROR_MESSAGE_PAGE_SIZE_TOO_HIGH =
            "Page size must be maximum $MAXIMUM_PAGE_SIZE!"
    }
}