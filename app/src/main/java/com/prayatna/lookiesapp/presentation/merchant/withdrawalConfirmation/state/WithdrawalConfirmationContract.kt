package com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation.state

import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest

data class WithdrawalConfirmationUiState(
    val isLoading: Boolean = false,
    val withdrawalRequest: WithdrawalRequest? = null,
    val errorMessage: String? = null,
    val isProcessing: Boolean = false
)

sealed interface WithdrawalConfirmationEvent {
    data class LoadDetails(val withdrawalId: String) : WithdrawalConfirmationEvent
    data object ProcessPayout : WithdrawalConfirmationEvent
    data object BackClicked : WithdrawalConfirmationEvent
}

sealed interface WithdrawalConfirmationEffect {
    data object NavigateBack : WithdrawalConfirmationEffect
    data class ShowMessage(val message: String) : WithdrawalConfirmationEffect
    data object NavigateToDashboard : WithdrawalConfirmationEffect
}
