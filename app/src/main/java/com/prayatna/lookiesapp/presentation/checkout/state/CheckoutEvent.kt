package com.prayatna.lookiesapp.presentation.checkout.state

import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.domain.model.user.UserAddress

sealed class CheckoutEvent {

    data class OnLoad(
        val type: String,
        val itemId: String,
        val quantity: Int
    ) : CheckoutEvent()

    data object OnBackClick : CheckoutEvent()
    data object OnAddAddressClick : CheckoutEvent()
    data object OnPayClick : CheckoutEvent()
    data object OnRefresh : CheckoutEvent()

    data class OnPaymentMethodSelected(val method: PaymentMethodUiState) : CheckoutEvent()
    data class OnShipmentFeeSelected(val fee: ShipmentFee) : CheckoutEvent()
    data class OnAddressSelected(val address: UserAddress) : CheckoutEvent()
}