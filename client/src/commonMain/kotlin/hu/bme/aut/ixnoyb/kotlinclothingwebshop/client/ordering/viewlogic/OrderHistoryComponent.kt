package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.OrderHistoryComponent.ViewState.HistoryLoaded
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.Order
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

interface OrderHistoryComponent {

    val viewState: StateFlow<ViewState>

    fun navigateBack()

    sealed interface ViewState {

        interface Loading : ViewState

        interface HistoryLoaded : ViewState {

            val orderHistory: List<Order>
        }
    }
}

internal class DefaultOrderHistoryComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val navigateBackAction: () -> Unit,
) : OrderHistoryComponent, ComponentContext by componentContext {

    // TODO
    override val viewState: StateFlow<OrderHistoryComponent.ViewState> = MutableStateFlow(
        object : HistoryLoaded {
            override val orderHistory: List<Order> = listOf(
                Order(
                    id = "1",
                    dateTime = LocalDateTime(
                        year = 2024,
                        monthNumber = 3,
                        dayOfMonth = 19,
                        hour = 15,
                        minute = 29,
                    ),
                    items = listOf(
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
                    ),
                ),
                Order(
                    id = "1",
                    dateTime = LocalDateTime(
                        year = 2024,
                        monthNumber = 3,
                        dayOfMonth = 19,
                        hour = 15,
                        minute = 29,
                    ),
                    items = listOf(
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
                    ),
                ),
                Order(
                    id = "1",
                    dateTime = LocalDateTime(
                        year = 2024,
                        monthNumber = 3,
                        dayOfMonth = 19,
                        hour = 15,
                        minute = 29,
                    ),
                    items = listOf(
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
                    ),
                ),
            )
        }
    )

    override fun navigateBack() {
        navigateBackAction()
    }
}