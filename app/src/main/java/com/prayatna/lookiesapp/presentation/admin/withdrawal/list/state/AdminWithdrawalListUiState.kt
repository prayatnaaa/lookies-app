package com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest

data class AdminWithdrawalListUiState(
    val withdrawalRequests: List<WithdrawalRequest> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
