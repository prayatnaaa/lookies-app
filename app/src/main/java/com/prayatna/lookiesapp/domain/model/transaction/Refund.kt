package com.prayatna.lookiesapp.domain.model.transaction

data class Refund(
    val id: String,
    val orderId: String,
    val userId: String,
    val amount: String,
    val bankCode: String,
    val accountNumber: String,
    val accountHolderName: String,
    val reason: String,
    val proofImageUrl: String?,
    val returnTrackingNumber: String?,
    val status: String,
    val adminNotes: String?,
    val xenditPayoutId: String?,
    val createdAt: String,
    val updatedAt: String
)
