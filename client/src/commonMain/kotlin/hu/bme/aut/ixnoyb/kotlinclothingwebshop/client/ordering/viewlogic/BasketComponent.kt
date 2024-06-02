package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BasketComponent {

    val viewState: StateFlow<ViewState>

    fun deleteBasketItem(itemID: String)

    fun navigateToOrderFinalization()

    sealed interface ViewState {

        interface Loading : ViewState

        interface BasketItemsAvailable : ViewState {

            val items: List<OrderItem>
        }
    }
}

internal class DefaultBasketComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateToOrderFinalizationAction: (List<String>) -> Unit = {},  // TODO use item IDs
) : BasketComponent, ComponentContext by componentContext {

    // TODO
    override val viewState: StateFlow<BasketComponent.ViewState> = MutableStateFlow(
        object : BasketComponent.ViewState.BasketItemsAvailable {

            // TODO
            override val items: List<OrderItem> = listOf(
                OrderItem(
                    id = "1",
                    imageUrl = "https://i.kym-cdn.com/photos/images/newsfeed/001/975/756/46f.jpg",
                    name = "The Legendary Drip Jacker Inspired by Goku",
                    price = 23000,
                ),
                OrderItem(
                    id = "2",
                    imageUrl = "https://static.wikia.nocookie.net/wowpedia/images/a/aa/Ian_Bates.jpg/revision/latest?cb=20130818034905",
                    name = "Red shirt",
                    price = 2500,
                ),
            )
        }
    )

    override fun deleteBasketItem(itemID: String) {
        // TODO
    }

    override fun navigateToOrderFinalization() {
        navigateToOrderFinalizationAction(listOf()) // TODO
    }
}