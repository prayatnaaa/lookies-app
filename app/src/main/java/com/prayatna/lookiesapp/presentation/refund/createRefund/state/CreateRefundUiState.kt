package com.prayatna.lookiesapp.presentation.refund.createRefund.state

import com.prayatna.lookiesapp.domain.model.transaction.Refund

sealed interface CreateRefundUiState {
    data object Idle : CreateRefundUiState
    data object Loading : CreateRefundUiState
    data class Success(val refund: Refund) : CreateRefundUiState
    data class Error(val message: String) : CreateRefundUiState
}
