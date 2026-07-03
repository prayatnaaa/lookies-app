package com.prayatna.lookiesapp.domain.model.withdrawal

data class WithdrawalRequest(
    val id: String,
    val merchantId: String,
    val amount: Long,
    val bankCode: String,
    val bankName: String? = null,
    val accountNumber: String,
    val accountName: String,
    val status: String,
    val createdAt: String,
    val adminNotes: String? = null,
    val xenditDisbursementId: String? = null,
    val updatedAt: String? = null,
    val processedAt: String? = null
)
