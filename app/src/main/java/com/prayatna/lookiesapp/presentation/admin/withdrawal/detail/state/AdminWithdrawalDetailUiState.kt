package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state

import com.prayatna.lookiesapp.domain.model.transaction.PayoutResult
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest

data class AdminWithdrawalDetailUiState(
    val isLoading: Boolean = false,
    val withdrawalRequest: WithdrawalRequest? = null,
    val errorMessage: String? = null,
    val payoutSuccessData: PayoutResult? = null
)
