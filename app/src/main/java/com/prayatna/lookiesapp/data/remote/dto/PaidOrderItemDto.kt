package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaidOrderItemDto(

    @SerialName("order_item_id")
    val orderItemId: String,

    @SerialName("event_id")
    val eventId: String,

    @SerialName("item_type")
    val itemType: String,

    @SerialName("quantity")
    val quantity: Int,

    @SerialName("unit_price")
    val unitPrice: Double,

    @SerialName("subtotal")
    val subtotal: Double,

    @SerialName("order_id")
    val orderId: String,

    @SerialName("payment_status")
    val paymentStatus: String,

    @SerialName("paid_at")
    val paidAt: String,

    @SerialName("buyer_name")
    val buyerName: String? = null
)