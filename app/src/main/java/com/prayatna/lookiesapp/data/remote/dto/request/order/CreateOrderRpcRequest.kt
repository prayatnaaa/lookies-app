package com.prayatna.lookiesapp.data.remote.dto.request.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRpcParams(
    @SerialName("p_buyer_id")
    val buyerId: String,
    @SerialName("p_currency")
    val currency: String = "IDR",
    @SerialName("p_items")
    val items: List<OrderItemRequest>,
    @SerialName("p_shipping_cost")
    val shippingCost: Int,
    @SerialName("p_recipient_name")
    val recipientName: String,
    @SerialName("p_phone_number")
    val phoneNumber: String,
    @SerialName("p_address_line")
    val addressLine: String,
    @SerialName("p_province")
    val province: String,
    @SerialName("p_postal_code")
    val postalCode: String,
)