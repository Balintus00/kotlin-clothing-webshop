package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.ordering.viewlogic.model

import kotlinx.datetime.LocalDateTime

data class Order(
    val id: String,
    val dateTime: LocalDateTime,
    val items: List<OrderItem>,
)