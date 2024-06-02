package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent.ViewState.ArticlePreviewsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.FeaturedArticlesComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.FeaturedArticleListStoreProvider
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import kotlinx.coroutines.flow.StateFlow

interface FeaturedArticlesComponent {

    val viewState: StateFlow<ViewState>

    fun navigateToArticleDetails(id: String)

    fun navigateToArticleSearch()

    sealed interface ViewState {

        interface Loading : ViewState

        interface ArticlePreviewsAvailable : ViewState {

            val articles: List<ArticlePreview>
        }
    }
}

internal class DefaultFeaturedArticlesComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateToArticleDetailsAction: (id: String) -> Unit = {},
    private val navigateToArticleSearchAction: () -> Unit = {},
) : FeaturedArticlesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        FeaturedArticleListStoreProvider(storeFactory).create()
    }

    override val viewState: StateFlow<ViewState> =
        store.getViewStateStateFlow(
            component = this,
            mapper = { it.toViewState() },
        )

    // TODO
    private fun State.toViewState(): ViewState = when (this) {
        is State.ArticleListContainerState.Loaded -> object : ArticlePreviewsAvailable {

            override val articles: List<ArticlePreview> =
                this@toViewState.articles.map { it.toUIArticlePreview() }
        }

        is State.ArticleListContainerState.Refreshing -> object : Loading {}
        is State.ArticleListContainerState.RefreshingFailed -> object : Loading {}
        is State.Loading -> object : Loading {}
        is State.LoadingFailed -> object : Loading {}
    }

    override fun navigateToArticleDetails(id: String) {
        navigateToArticleDetailsAction(id)
    }

    override fun navigateToArticleSearch() {
        navigateToArticleSearchAction()
    }
}