package com.prayatna.lookiesapp.domain.model.transaction

data class PaidOrderItem(
    val orderItemId: String,
    val eventId: String,
    val itemType: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val orderId: String,
    val paymentStatus: String,
    val paidAt: String,
    val buyerName: String?
)
