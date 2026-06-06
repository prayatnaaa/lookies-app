package com.prayatna.lookiesapp.domain.model.transaction

data class Transaction(
    val id: String,
    val merchantId: String,
    val buyerId: String,
    val totalAmount: Double,
    val currency: String,
    val status: String,
    val createdAt: String,
    val paymentInfo: PaymentInfo? = null,
    val items: List<TransactionItem>
)

data class PaymentInfo(
    val paymentId: String? = null,
    val invoiceId: String? = null,
    val status: String? = null,
    val provider: String? = null,
    val channel: String? = null
)