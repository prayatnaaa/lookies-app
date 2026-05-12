package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest

data class MerchantWithdrawalRequestListUiState(
    val isLoading: Boolean = false,
    val withdrawalRequests: List<WithdrawalRequest> = emptyList(),
    val errorMessage: String = ""
)