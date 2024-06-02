package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store

import com.arkivanov.mvikotlin.core.store.Store
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStore.Intent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStore.State

interface FeaturedArticleListStore : Store<Intent, State, Nothing> {

    sealed interface Intent {

        data object Refresh : Intent

        data object Retry : Intent
    }

    sealed interface State {

        data object Loading : State

        data object LoadingFailed : State

        sealed interface ArticleListContainerState : State {

            val articles: List<ArticlePreview>

            fun getWithChangedArticles(
                changedArticles: List<ArticlePreview>,
            ): ArticleListContainerState

            data class Loaded(
                override val articles: List<ArticlePreview>,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class Refreshing(
                override val articles: List<ArticlePreview>,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles = changedArticles)
            }

            data class RefreshingFailed(
                override val articles: List<ArticlePreview>,
            ) : ArticleListContainerState {

                override fun getWithChangedArticles(
                    changedArticles: List<ArticlePreview>,
                ): ArticleListContainerState = this.copy(articles =  changedArticles)
            }
        }
    }
}