package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state

data class CreateWithdrawalRequestUiState(
    val isLoading: Boolean = false,
    val amount: String = "",
    val bankCode: String = "",
    val accountNumber: String = "",
    val accountName: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)
