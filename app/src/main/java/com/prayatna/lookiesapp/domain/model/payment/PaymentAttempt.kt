package com.prayatna.lookiesapp.domain.model.payment

data class PaymentAttempt(
    val id: String,
    val orderId: String,
    val provider: String,
    val status: String,
    val channel: String,
    val externalId: String,
    val createdAt: String,
    val amount: String,
    val currency: String,
    val failureReason: String? = null,
    val updatedAt: String? = null
)