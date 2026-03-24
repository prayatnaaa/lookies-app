package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentAttemptDto(
    val id: String,
    @SerialName("order_id")
    val orderId: String,
    val provider: String,
    val status: String,
    val channel: String,
    @SerialName("external_id")
    val externalId: String,
    @SerialName("created_at")
    val createdAt: String,
    val amount: Int,
    val currency: String,
    @SerialName("failure_reason")
    val failureReason: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)