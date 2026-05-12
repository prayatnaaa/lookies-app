package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state

sealed interface CreateWithdrawalRequestEffect {
    data object NavigateBack : CreateWithdrawalRequestEffect
    data class ShowMessage(val message: String) : CreateWithdrawalRequestEffect
}
