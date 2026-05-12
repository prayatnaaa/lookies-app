package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state

sealed interface CreateWithdrawalRequestEvent {
    data class AmountChanged(val amount: String) : CreateWithdrawalRequestEvent
    data class BankCodeChanged(val bankCode: String) : CreateWithdrawalRequestEvent
    data class AccountNumberChanged(val accountNumber: String) : CreateWithdrawalRequestEvent
    data class AccountNameChanged(val accountName: String) : CreateWithdrawalRequestEvent
    data object SubmitClicked : CreateWithdrawalRequestEvent
    data object BackClicked : CreateWithdrawalRequestEvent
    data object DismissDialog : CreateWithdrawalRequestEvent
}
