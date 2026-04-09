package com.prayatna.lookiesapp.presentation.checkout.state

sealed class CheckoutEvent {
    data object OnBackClick : CheckoutEvent()
    data object OnPayClick : CheckoutEvent()
    data object OnRefresh : CheckoutEvent()
    data class OnPaymentMethodSelected(val method: PaymentMethodUiState) : CheckoutEvent()

    data object OnSuccessConfirmed : CheckoutEvent()
    data object OnErrorConfirmed : CheckoutEvent()
}