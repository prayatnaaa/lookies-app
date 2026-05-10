package com.prayatna.lookiesapp.domain.model.transaction

data class OrderSplit(
    val id: String,
    val orderId: String,
    val merchantId: String,
    val grossAmount: Double,
    val platformFee: Double,
    val netAmount: Double,
    val payoutStatus: String,
    val payoutReference: String?,
    val createdAt: String
)
