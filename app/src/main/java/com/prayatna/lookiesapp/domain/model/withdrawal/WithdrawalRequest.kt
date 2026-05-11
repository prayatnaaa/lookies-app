package com.prayatna.lookiesapp.domain.model.withdrawal

data class WithdrawalRequest(
    val id: String,
    val merchantId: String,
    val amount: Long,
    val bankCode: String,
    val accountNumber: String,
    val accountName: String,
    val status: String,
    val createdAt: String,
    val adminNotes: String? = null,
    val xenditDisbursementId: String,
    val updatedAt: String,
    val processedAt: String
)
