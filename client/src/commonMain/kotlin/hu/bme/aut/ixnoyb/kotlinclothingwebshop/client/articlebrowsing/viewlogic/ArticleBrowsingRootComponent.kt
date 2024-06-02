package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleBrowsingRootComponent.Child
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.DefaultArticleBrowsingRootComponent.Config.ArticleDetails
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.DefaultArticleBrowsingRootComponent.Config.FeaturedArticles
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.DefaultArticleBrowsingRootComponent.Config.SearchArticles
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.DefaultArticleBrowsingRootComponent.Config.SearchArticlesResult
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.ArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.model.toDomainArticleFilter
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.toStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.article.ArticleID
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface ArticleBrowsingRootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {

        class ArticleDetails(val component: ArticleDetailsComponent) : Child

        class FeaturedArticles(val component: FeaturedArticlesComponent) : Child

        class SearchArticles(val component: SearchArticlesComponent) : Child

        class SearchArticlesResult(val component: SearchArticlesResultComponent) : Child
    }
}

internal class DefaultArticleBrowsingRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val onArticleSuccessfullyBasketToAddedAction: () -> Unit = {},
) : ArticleBrowsingRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: StateFlow<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = FeaturedArticles,
        handleBackButton = true,
        childFactory = ::createChild,
    ).toStateFlow()

    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        val pushNewArticleDetailsAction: (String, Boolean) -> Unit = { id, isRecommended ->
            navigation.pushNew(ArticleDetails(id, isRecommended))
        }

        return when (config) {
            is ArticleDetails -> {
                Child.ArticleDetails(
                    DefaultArticleDetailsComponent(
                        articleID = ArticleID(config.articleID),
                        componentContext = componentContext,
                        isRecommendedArticle = config.isRecommendedArticle,
                        navigateBackAction = { navigation.popWhile { it is ArticleDetails } },
                        onSuccessfullyAddedToBasketAction =
                        onArticleSuccessfullyBasketToAddedAction,
                        storeFactory = storeFactory,
                    )
                )
            }

            is FeaturedArticles -> {
                Child.FeaturedArticles(
                    DefaultFeaturedArticlesComponent(
                        componentContext = componentContext,
                        navigateToArticleDetailsAction = {
                            pushNewArticleDetailsAction(it, true)
                        },
                        navigateToArticleSearchAction = {
                            navigation.pushNew(SearchArticles)
                        },
                        storeFactory = storeFactory,
                    )
                )
            }

            is SearchArticles -> {
                Child.SearchArticles(
                    DefaultSearchArticlesComponent(
                        componentContext = componentContext,
                        navigateBackAction = { navigation.popWhile { it is SearchArticles } },
                        navigateToSearchResultsAction = {
                            navigation.pushNew(SearchArticlesResult(it))
                        },
                    )
                )
            }

            is SearchArticlesResult -> {
                Child.SearchArticlesResult(
                    DefaultSearchArticlesResultComponent(
                        componentContext = componentContext,
                        filter = config.filter.toDomainArticleFilter(),
                        navigateBackAction = {
                            navigation.popWhile { (it is SearchArticles).not() }
                        },
                        navigateToArticleDetailsAction = {
                            pushNewArticleDetailsAction(it, false)
                        },
                        storeFactory = storeFactory
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data class ArticleDetails(val articleID: String, val isRecommendedArticle: Boolean) : Config

        @Serializable
        data object FeaturedArticles : Config

        @Serializable
        data object SearchArticles : Config

        @Serializable
        data class SearchArticlesResult(val filter: ArticleFilter) : Config
    }
}