package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleLocalDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.datasource.ArticleRemoteDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.datasource.UserPersistentSecureDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.paging.ArticlePageSpecification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal interface ArticleRepository {

    fun getArticleByID(articleID: ArticleID): Flow<Article?>

    fun getSearchedArticlesPreviews(): Flow<List<ArticlePreview>>

    fun getRecommendedArticlePreviews(): Flow<List<ArticlePreview>>

    suspend fun loadRecommendedArticleByID(articleID: ArticleID): Article

    suspend fun loadSearchedArticleByID(articleID: ArticleID): Article

    suspend fun loadSearchedArticlePreviews(
        filter: ArticleFilter,
        pageSpecification: ArticlePageSpecification,
    ): Pair<List<ArticlePreview>, Boolean>

    suspend fun loadRecommendedArticlePreviews(): List<ArticlePreview>
}

internal class DefaultArticleRepository(
    private val coroutineScope: CoroutineScope,
    private val remoteDatasource: ArticleRemoteDatasource,
    private val persistentDatasource: ArticleLocalDatasource,
    private val userPersistentSecureDatasource: UserPersistentSecureDatasource,
    private val transientDatasource: ArticleLocalDatasource,
) : ArticleRepository {

    override fun getArticleByID(articleID: ArticleID): Flow<Article?> =
        persistentDatasource.getByID(articleID).combine(
            transientDatasource.getByID(articleID)
        ) { persistentArticle, transientArticle ->
            when {
                transientArticle != null -> transientArticle
                persistentArticle != null -> persistentArticle
                else -> null
            }
        }.flowOn(Dispatchers.Default)

    override fun getSearchedArticlesPreviews(): Flow<List<ArticlePreview>> =
        transientDatasource.getAllPreviews().flowOn(Dispatchers.Default)

    override fun getRecommendedArticlePreviews(): Flow<List<ArticlePreview>> =
        persistentDatasource.getAllPreviews().flowOn(Dispatchers.Default)

    override suspend fun loadRecommendedArticleByID(articleID: ArticleID): Article =
        withContext(Dispatchers.Default) {
            try {
                remoteDatasource.getByID(articleID).also { persistentDatasource.update(it) }
            } catch (t: Throwable) {
                if (t.message == ArticleRemoteDatasource.ERROR_MESSAGE_NOT_FOUND) {
                    throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
                } else {
                    throw IllegalStateException(ERROR_MESSAGE_GENERAL_ERROR)
                }
            }
        }

    override suspend fun loadSearchedArticleByID(articleID: ArticleID): Article =
        withContext(Dispatchers.Default) {
            try {
                remoteDatasource.getByID(articleID).also {
                    transientDatasource.update(it)

                    coroutineScope.launch {
                        persistentDatasource.update(it)
                    }
                }
            } catch (t: Throwable) {
                if (t.message == ArticleRemoteDatasource.ERROR_MESSAGE_NOT_FOUND) {
                    throw IllegalArgumentException(ERROR_MESSAGE_ARTICLE_NOT_FOUND)
                } else {
                    throw IllegalStateException(ERROR_MESSAGE_GENERAL_ERROR)
                }
            }
        }

    override suspend fun loadSearchedArticlePreviews(
        filter: ArticleFilter,
        pageSpecification: ArticlePageSpecification,
    ): Pair<List<ArticlePreview>, Boolean> = withContext(Dispatchers.Default) {
        try {
            val articles = loadAndSaveArticlePreviews(
                articleClearAction = {
                    if (pageSpecification.lastReceivedArticleID == null) {
                        transientDatasource.clear()
                    }
                },
                articleInsertAction = { previewList ->
                    transientDatasource.insertPreviews(*previewList.toTypedArray())
                },
                articleLoadingAction = {
                    remoteDatasource.getArticlePreviews(
                        filter,
                        pageSpecification
                    )
                },
            )

            val isNextPagePossiblyExists = articles.size == pageSpecification.size.value

            articles to isNextPagePossiblyExists
        } catch (t: Throwable) {
            throw IllegalStateException(ERROR_MESSAGE_GENERAL_ERROR)
        }
    }

    private inline fun loadAndSaveArticlePreviews(
        articleLoadingAction: () -> List<ArticlePreview>,
        articleClearAction: () -> Unit,
        articleInsertAction: (articles: List<ArticlePreview>) -> Unit,
    ) = articleLoadingAction().also {
        articleClearAction()
        articleInsertAction(it)
    }

    override suspend fun loadRecommendedArticlePreviews(): List<ArticlePreview> =
        withContext(Dispatchers.Default) {
            try {
                loadAndSaveArticlePreviews(
                    articleClearAction = { persistentDatasource.clear() },
                    articleInsertAction = { previewList ->
                        persistentDatasource.insertPreviews(*previewList.toTypedArray())
                    },
                    articleLoadingAction = {
                        remoteDatasource.getRecommendedArticlePreviews(
                            authenticationToken = userPersistentSecureDatasource
                                .authenticationToken.value,
                        )
                    },
                )
            } catch (t: Throwable) {
                throw IllegalStateException(ERROR_MESSAGE_GENERAL_ERROR)
            }
        }

    companion object {

        const val ERROR_MESSAGE_ARTICLE_NOT_FOUND = "ERROR_MESSAGE_ARTICLE_NOT_FOUND"
        const val ERROR_MESSAGE_GENERAL_ERROR = "ERROR_MESSAGE_GENERAL_ERROR"
    }
}