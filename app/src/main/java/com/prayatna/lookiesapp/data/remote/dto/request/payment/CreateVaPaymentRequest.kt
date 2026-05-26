package com.prayatna.lookiesapp.data.remote.dto.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateVaPaymentRequest(
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("order_id")
    val orderId: String,
    val amount: Int,
    @SerialName("channel_code")
    val channelCode: String,
    @SerialName("customer_name")
    val customerName: String,
    @SerialName("expires_at")
    val expiresAt: String,
    val description: String? = null
)
