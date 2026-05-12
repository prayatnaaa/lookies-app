package com.prayatna.lookiesapp.domain.model.withdrawal

data class CreateWithdrawalRequestInput(
    val merchantId: String,
    val amount: Long,
    val bankCode: String,
    val accountNumber: String,
    val accountName: String
)
