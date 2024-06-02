package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model

data class OrderItem(
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: Int,
)