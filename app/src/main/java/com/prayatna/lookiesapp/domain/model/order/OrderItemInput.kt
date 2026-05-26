package com.prayatna.lookiesapp.domain.model.order

data class OrderItemInput(
    val itemType: String,
    val itemRefId: String,
    val quantity: Int = 1
)