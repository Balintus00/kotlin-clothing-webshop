package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.ArticlePreviewsAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.SearchArticlesResultComponent.ViewState.Loading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIArticlePreview
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.LastPageLoaded
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.NextPageLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.NextPageLoadingFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.PageLoadedWithPossibleNextPages
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.Refreshing
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.ArticleListContainerState.RefreshingFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.FirstPageLoading
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStore.State.FirstPageLoadingFailed
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.SearchArticleListStoreProvider
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import kotlinx.coroutines.flow.StateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleFilter as DomainArticleFilter

interface SearchArticlesResultComponent {

    val viewState: StateFlow<ViewState>

    fun navigateBack()

    fun navigateToArticleDetails(articleID: String)

    sealed interface ViewState {

        interface Loading : ViewState

        interface ArticlePreviewsAvailable : ViewState {

            val articles: List<ArticlePreview>
        }
    }
}

internal class DefaultSearchArticlesResultComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val filter: DomainArticleFilter,
    private val navigateBackAction: () -> Unit = {},
    private val navigateToArticleDetailsAction: (String) -> Unit = {},
) : SearchArticlesResultComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        SearchArticleListStoreProvider(filter, storeFactory).create()
    }

    override val viewState: StateFlow<ViewState> = store.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    // TODO
    private fun SearchArticleListStore.State.toViewState(): ViewState = when (this) {
        is LastPageLoaded -> object : ArticlePreviewsAvailable {

            override val articles: List<ArticlePreview> = this@toViewState.articles.map {
                it.toUIArticlePreview()
            }
        }

        is NextPageLoading -> object : Loading {}
        is NextPageLoadingFailed -> object : Loading {}
        is PageLoadedWithPossibleNextPages -> object : ArticlePreviewsAvailable {

            override val articles: List<ArticlePreview> = this@toViewState.articles.map {
                it.toUIArticlePreview()
            }
        }

        is Refreshing -> object : Loading {}
        is RefreshingFailed -> object : Loading {}
        is FirstPageLoading -> object : Loading {}
        is FirstPageLoadingFailed -> object : Loading {}
    }

    override fun navigateBack() {
        navigateBackAction()
    }

    override fun navigateToArticleDetails(articleID: String) {
        navigateToArticleDetailsAction(articleID)
    }
}