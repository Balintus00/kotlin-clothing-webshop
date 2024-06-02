package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID

internal interface SearchArticleListStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data object LoadNextPage : Intent

        data object Refresh : Intent

        data object Retry : Intent
    }

    sealed interface State {

        data object FirstPageLoading : State

        data object FirstPageLoadingFailed : State

        sealed interface ArticleListContainerState : State {

            val articles: List<ArticlePreview>

            val lastLoadedArticleID: ArticleID?

            fun getWithChangedArticles(changedArticles: List<ArticlePreview>): ArticleListContainerState

            data class PageLoadedWithPossibleNextPages(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class NextPageLoading(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class NextPageLoadingFailed(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class LastPageLoaded(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class Refreshing(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
                val isLastPageLoaded: Boolean,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class RefreshingFailed(
                override val articles: List<ArticlePreview>,
                override val lastLoadedArticleID: ArticleID?,
                val isLastPageLoaded: Boolean,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles =  changedArticles)
            }
        }
    }
}