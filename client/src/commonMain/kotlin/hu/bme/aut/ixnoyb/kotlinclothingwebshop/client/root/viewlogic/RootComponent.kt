package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.root.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.thelordoftheringscharacterwiki.viewlogic.utility.toStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface RootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    fun selectArticleBrowserFeature()
    fun selectCheckoutFeature()
    fun selectUserManagementFeature()

    sealed interface Child {

        class ArticleBrowserFeature() : Child

        class CheckoutFeature() : Child

        class UserManagementFeature() : Child
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
    ): RootComponent.Child = when(config) {
        Config.ArticleBrowserFeature -> RootComponent.Child.ArticleBrowserFeature()
        Config.CheckoutFeature -> RootComponent.Child.CheckoutFeature()
        Config.UserManagementFeature -> RootComponent.Child.UserManagementFeature()
    }


    override fun selectArticleBrowserFeature() {
        navigation.replaceAll(Config.ArticleBrowserFeature)
    }

    override fun selectCheckoutFeature() {
        navigation.replaceAll(Config.CheckoutFeature)
    }

    override fun selectUserManagementFeature() {
        navigation.replaceAll(Config.UserManagementFeature)
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ArticleBrowserFeature : Config
        @Serializable

        data object CheckoutFeature : Config
        @Serializable

        data object UserManagementFeature : Config
    }
}