package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.viewlogic.toStateFlow
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderingRootComponent.Child
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface OrderingRootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {

        class Basket(val component: BasketComponent) : Child

        class OrderFinalization(val component: FinalizeOrderComponent) : Child
    }
}

internal class DefaultOrderingRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val onSuccessfulOrderingAction: () -> Unit,
) : OrderingRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: StateFlow<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Basket,
        handleBackButton = true,
        childFactory = ::createChild,
    ).toStateFlow()

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Basket -> {
                Child.Basket(
                    DefaultBasketComponent(
                        componentContext = componentContext,
                        navigateToOrderFinalizationAction = {
                            navigation.pushNew(Config.OrderFinalization(it))
                        },
                        storeFactory = storeFactory,
                    )
                )
            }

            is Config.OrderFinalization -> {
                Child.OrderFinalization(
                    DefaultFinalizeOrderComponent(
                        componentContext = componentContext,
                        navigateBackAction = {
                            navigation.popWhile { it is Config.OrderFinalization }
                        },
                        onSuccessfulOrderFinalizationAction = onSuccessfulOrderingAction,
                        storeFactory = storeFactory,
                    )
                )
            }
        }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Basket : Config

        @Serializable
        data class OrderFinalization(val itemIDs: List<String>) : Config
    }
}