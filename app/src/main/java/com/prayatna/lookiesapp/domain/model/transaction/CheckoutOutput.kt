package com.prayatna.lookiesapp.domain.model.transaction

data class CheckoutOutput(
    val orderId: Int,
    val transactionId: String,
    val status: String
)