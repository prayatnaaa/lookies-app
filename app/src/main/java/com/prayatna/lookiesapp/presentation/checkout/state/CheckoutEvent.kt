package com.prayatna.lookiesapp.presentation.checkout.state

import com.prayatna.lookiesapp.domain.model.transaction.ShipmentFee
import com.prayatna.lookiesapp.domain.model.user.UserAddress

sealed class CheckoutEvent {
    data object OnBackClick : CheckoutEvent()
    data object OnPayClick : CheckoutEvent()
    data object OnRefresh : CheckoutEvent()
    data class OnPaymentMethodSelected(val method: PaymentMethodUiState) : CheckoutEvent()
    data class OnShipmentFeeSelected(val fee: ShipmentFee) : CheckoutEvent()
    data class OnAddressSelected(val address: UserAddress) : CheckoutEvent()

    data object OnSuccessConfirmed : CheckoutEvent()
    data object OnErrorConfirmed : CheckoutEvent()
}