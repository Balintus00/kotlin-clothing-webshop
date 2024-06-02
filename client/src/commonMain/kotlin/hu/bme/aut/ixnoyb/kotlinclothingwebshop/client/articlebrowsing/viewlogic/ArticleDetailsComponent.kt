package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.repository.model.Article as DomainArticle
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleDetailsComponent.ViewState
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.Article
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIColor
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIGarmentGroup
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIGraphicalAppearance
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIIndex
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toUIShade
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStore.State
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.store.ArticleDetailsStoreProvider
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.createAndGetStore
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.getViewStateStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.flow.StateFlow

interface ArticleDetailsComponent {

    val viewState: StateFlow<ViewState>

    interface ViewState {

        fun navigateBack()

        interface Loading : ViewState

        interface Loaded : ViewState {

            val article: Article

            fun addToBasket()
        }

        interface LoadingFailed : ViewState {

            fun retryLoading()
        }
    }
}

class DefaultArticleDetailsComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val articleID: ArticleID,
    private val isRecommendedArticle: Boolean,
    private val navigateBackAction: () -> Unit = {},
    private val onSuccessfullyAddedToBasketAction: () -> Unit = {},
) : ArticleDetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.createAndGetStore {
        ArticleDetailsStoreProvider(
            articleID = articleID,
            isRecommendedArticle = isRecommendedArticle,
            storeFactory = storeFactory,
        ).create()
    }

    override val viewState: StateFlow<ViewState> = store.getViewStateStateFlow(
        component = this,
        mapper = { it.toViewState() },
    )

    private fun State.toViewState(): ViewState = when (this) {
        is State.Loaded -> object : ViewState.Loaded {

            override val article: Article = this@toViewState.article.toUIArticle()

            private fun DomainArticle.toUIArticle(): Article = Article(
                id = id.value,
                brand = brand.value,
                color = color.toUIColor(),
                description = description.value,
                garmentGroup = garmentGroup.toUIGarmentGroup(),
                graphicalAppearance = graphicalAppearance.toUIGraphicalAppearance(),
                imageUrl = imageUrl.toString(),
                index = index.toUIIndex(),
                name = name.value,
                price = price.value,
                shade = shade.toUIShade(),
            )

            override fun addToBasket() {
                // TODO
                onSuccessfullyAddedToBasketAction()
            }

            override fun navigateBack() {
                navigateBackAction()
            }
        }

        is State.Loading -> object : ViewState.Loading {

            override fun navigateBack() {
                navigateBackAction()
            }
        }

        is State.LoadingFailed -> object : ViewState.LoadingFailed {

            override fun navigateBack() {
                navigateBackAction()
            }

            override fun retryLoading() {
                store.accept(ArticleDetailsStore.Intent.Retry)
            }
        }
    }
}