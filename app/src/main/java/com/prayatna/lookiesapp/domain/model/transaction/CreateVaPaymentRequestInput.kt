package com.prayatna.lookiesapp.domain.model.transaction

data class CreateVaPaymentRequestInput(
    val merchantId: String,
    val orderId: String,
    val amount: Int,
    val channelCode: String,
    val customerName: String,
    val expiresAt: String,
    val description: String? = null
)
