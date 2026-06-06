package com.prayatna.lookiesapp.domain.model.payment

data class PaymentAttempt(
    val id: String,
    val orderId: String,
    val provider: String,
    val channel: String,
    val externalId: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val failureReason: String?,
    val createdAt: String,
    val updatedAt: String?,
    val paymentRequestId: String?,
    val redirectUrl: String?,
    val virtualAccountNumber: String? = null,
    val qrString: String? = null,
    val customerName: String? = null,
    val expiresAt: String? = null
)
