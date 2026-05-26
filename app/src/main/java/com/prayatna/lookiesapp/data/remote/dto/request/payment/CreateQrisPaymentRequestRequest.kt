package com.prayatna.lookiesapp.data.remote.dto.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQrisPaymentRequestRequest (
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("order_id")
    val orderId: String,
    val amount: Int,
    val description: String? = null
)