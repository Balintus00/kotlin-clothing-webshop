package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.ArticleBrowsingRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.viewlogic.DefaultArticleBrowsingRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.toStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.DefaultOrderHistoryComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.DefaultOrderingRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderHistoryComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderingRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.DefaultUserManagementRootComponent
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.UserManagementRootComponent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface RootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    fun selectArticleBrowserFeature()
    fun selectOrderingFeature()
    fun selectUserManagementFeature()

    sealed interface Child {

        class ArticleBrowserFeature(val component: ArticleBrowsingRootComponent) : Child

        class OrderingFeature(val component: OrderingRootComponent) : Child

        class OrderHistoryFeature(val component: OrderHistoryComponent) : Child

        class UserManagementFeature(val component: UserManagementRootComponent) : Child
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: StateFlow<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.ArticleBrowserFeature,
        handleBackButton = true,
        childFactory = ::createChild,
    ).toStateFlow()

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        Config.ArticleBrowserFeature -> {
            RootComponent.Child.ArticleBrowserFeature(
                DefaultArticleBrowsingRootComponent(
                    componentContext = componentContext,
                    onArticleSuccessfullyBasketToAddedAction = {
                        navigation.replaceAll(Config.OrderingFeature)
                    },
                    storeFactory = storeFactory,
                )
            )
        }

        Config.OrderHistoryFeature -> {
            RootComponent.Child.OrderHistoryFeature(
                DefaultOrderHistoryComponent(
                    navigateBackAction = {
                        navigation.popWhile { it is Config.OrderHistoryFeature }
                    },
                    componentContext = componentContext,
                    storeFactory = storeFactory,
                )
            )
        }

        Config.OrderingFeature -> {
            RootComponent.Child.OrderingFeature(
                DefaultOrderingRootComponent(
                    componentContext = componentContext,
                    onSuccessfulOrderingAction = {
                        navigation.replaceAll(Config.UserManagementFeature)
                        navigation.pushNew(Config.OrderHistoryFeature)
                    },
                    storeFactory = storeFactory,
                )
            )
        }

        Config.UserManagementFeature -> {
            RootComponent.Child.UserManagementFeature(
                DefaultUserManagementRootComponent(
                    componentContext = componentContext,
                    navigateToPurchaseHistoryAction = {
                        navigation.pushNew(Config.OrderHistoryFeature)
                    },
                    storeFactory = storeFactory
                )
            )
        }
    }


    override fun selectArticleBrowserFeature() {
        navigation.replaceAll(Config.ArticleBrowserFeature)
    }

    override fun selectOrderingFeature() {
        navigation.replaceAll(Config.OrderingFeature)
    }

    override fun selectUserManagementFeature() {
        navigation.replaceAll(Config.UserManagementFeature)
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ArticleBrowserFeature : Config

        @Serializable
        data object OrderingFeature : Config

        @Serializable
        data object OrderHistoryFeature : Config

        @Serializable
        data object UserManagementFeature : Config
    }
}