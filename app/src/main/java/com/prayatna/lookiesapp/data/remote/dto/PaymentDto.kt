package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentDto(
    val id: Int?,
    @SerialName("user_id") val userId: Int,
    val amount: Double,
    val status: String,
    @SerialName("payment_type") val paymentType: String,
    val quantity: Int = 1
)
