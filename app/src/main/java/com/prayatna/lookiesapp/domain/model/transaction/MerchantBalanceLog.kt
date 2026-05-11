package com.prayatna.lookiesapp.domain.model.transaction

data class MerchantBalanceLog(
    val id: String,
    val merchantId: String,
    val transactionType: String,
    val amount: Long,
    val balanceBefore: Long,
    val balanceAfter: Long,
    val refId: String,
    val orderId: String,
    val eventId: Int? = null,
    val description: String?,
    val createdAt: String
)
