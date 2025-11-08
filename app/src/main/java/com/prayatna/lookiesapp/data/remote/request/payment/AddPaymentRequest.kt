package com.prayatna.lookiesapp.data.remote.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddPaymentRequest (
    @SerialName("user_id") val userId: String?,
    val amount: Double,
    @SerialName("payment_for_type") val paymentType: String,
    val quantity: Int
    )