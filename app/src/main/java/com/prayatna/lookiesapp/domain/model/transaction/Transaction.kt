package com.prayatna.lookiesapp.domain.model.transaction

data class Transaction(
    val id: String,
    val buyerId: String,
    val totalAmount: Double,
    val currency: String,
    val status: String,
    val createdAt: String,
    val paymentInfo: PaymentInfo? = null,
    val items: List<TransactionItem>
)