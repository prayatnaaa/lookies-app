package com.prayatna.lookiesapp.domain.model.transaction

data class CreateRefundRequestInput(
    val orderId: String,
    val userId: String,
    val amount: String,
    val bankCode: String,
    val accountNumber: String,
    val accountHolderName: String,
    val reason: String,
    val proofImageUrl: String? = null,
    val returnTrackingNumber: String? = null,
    val adminNotes: String? = null,
    val xenditPayoutId: String? = null
)
