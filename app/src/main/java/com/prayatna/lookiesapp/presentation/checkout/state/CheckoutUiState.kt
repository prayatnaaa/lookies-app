package com.prayatna.lookiesapp.presentation.checkout.state

import com.prayatna.lookiesapp.domain.model.transaction.ShipmentFee

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val itemToBuy: CheckoutItemDisplay? = null,
    val checkoutSuccessData: String? = null,
    val selectedMethod: PaymentMethodUiState = PaymentMethodUiState.QRIS,
    val shipmentFees: List<ShipmentFee> = emptyList(),
    val selectedShipmentFee: ShipmentFee? = null,
    val isShipmentLoading: Boolean = false
)

data class CheckoutItemDisplay(
    val id: String,
    val merchantId: String,
    val title: String,
    val subtitle: String,
    val price: Double?,
    val imageUrl: String,
    val type: String
)