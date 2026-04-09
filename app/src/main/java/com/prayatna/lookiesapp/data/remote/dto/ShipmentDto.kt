package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShipmentDto(
    @SerialName("id")
    val id: String,
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("tracking_number")
    val trackingNumber: String,
    @SerialName("status")
    val status: String,
    @SerialName("shipping_cost")
    val shippingCost: Double,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("shipped_at")
    val shippedAt: String? = null,
)