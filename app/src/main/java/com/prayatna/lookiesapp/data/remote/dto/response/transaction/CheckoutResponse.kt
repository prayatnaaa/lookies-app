package com.prayatna.lookiesapp.data.remote.dto.response.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    @SerialName("order_id") val orderId: Int,
    @SerialName("transaction_id") val transactionId: String,
    val status: String
)