package com.prayatna.lookiesapp.presentation.offlineCheckout.state

import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState

sealed interface OfflineCheckoutEvent {
    data class OnLoad(val itemId: String, val quantity: Int) : OfflineCheckoutEvent
    data object OnPayClick : OfflineCheckoutEvent
    data object OnBackClick : OfflineCheckoutEvent
    data object OnRefresh : OfflineCheckoutEvent
    data object DismissError : OfflineCheckoutEvent
    data class OnPaymentMethodSelected(val method: PaymentMethodUiState) : OfflineCheckoutEvent
    data class OnBankCodeSelected(val bankCode: String) : OfflineCheckoutEvent
}
