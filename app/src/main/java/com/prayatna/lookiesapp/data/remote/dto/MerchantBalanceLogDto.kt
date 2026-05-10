package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantBalanceLogDto(
    val id: String,
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("transaction_type")
    val transactionType: String,
    val amount: Long,
    @SerialName("balance_before")
    val balanceBefore: Long,
    @SerialName("balance_after")
    val balanceAfter: Long,
    @SerialName("reference_id")
    val refId: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("event_id")
    val eventId: Int,
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String
)