package com.prayatna.lookiesapp.presentation.refund.createRefund.state

import android.net.Uri

sealed class CreateRefundEvent {
    data class OrderIdChanged(val value: String) : CreateRefundEvent()
    data class AmountChanged(val value: String) : CreateRefundEvent()
    data class BankCodeChanged(val value: String) : CreateRefundEvent()
    data class AccountNumberChanged(val value: String) : CreateRefundEvent()
    data class AccountHolderNameChanged(val value: String) : CreateRefundEvent()
    data class ReasonChanged(val value: String) : CreateRefundEvent()
    data class ProofImageSelected(val uri: Uri) : CreateRefundEvent()
    data object Submit : CreateRefundEvent()
    data object DismissError : CreateRefundEvent()
    data object OnBackClick : CreateRefundEvent()
}
