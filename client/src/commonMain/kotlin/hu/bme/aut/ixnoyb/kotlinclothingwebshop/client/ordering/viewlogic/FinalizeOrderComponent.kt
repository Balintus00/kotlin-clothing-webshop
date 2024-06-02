package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.FinalizeOrderComponent.ViewState.AmountAvailable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface FinalizeOrderComponent {

    val viewState: StateFlow<ViewState>

    fun navigateBack()

    fun placeOrder(destination: OrderAddress)

    sealed interface ViewState {

        interface LoadingAmount : ViewState

        interface AmountAvailable : ViewState {

            val totalAmount: Int
        }
    }
}

internal class DefaultFinalizeOrderComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit = {},
    private val onSuccessfulOrderFinalizationAction: () -> Unit = {},
) : FinalizeOrderComponent, ComponentContext by componentContext {

    // TODO
    override val viewState: StateFlow<FinalizeOrderComponent.ViewState> = MutableStateFlow(
        object : AmountAvailable {
            override val totalAmount: Int
                get() = 25500
        }
    )

    override fun navigateBack() {
        navigateBackAction()
    }

    override fun placeOrder(destination: OrderAddress) {
        // TODO
        onSuccessfulOrderFinalizationAction()
    }
}