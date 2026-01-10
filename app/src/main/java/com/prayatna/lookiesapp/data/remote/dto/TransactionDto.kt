package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: String,

    @SerialName("buyer_id")
    val buyerId: String,

    @SerialName("total_amount")
    val totalAmount: Double,

    val currency: String,

    val status: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("payment_info")
    val paymentInfo: PaymentInfoDto? = null,

    val items: List<TransactionItemDto>
)