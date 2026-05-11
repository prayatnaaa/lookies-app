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
    val trackingNumber: String? = null,
    @SerialName("status")
    val status: String,
    @SerialName("shipping_cost")
    val shippingCost: Double,
    @SerialName("reciepent_name")
    val reciepentName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("address_line")
    val addressLine: String,
    @SerialName("province")
    val province: String,
    @SerialName("postal_code")
    val postalCode: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("shipped_at")
    val shippedAt: String? = null,
    @SerialName("arrival_proof_url")
    val arrivalProofUrl: String? = null,
)