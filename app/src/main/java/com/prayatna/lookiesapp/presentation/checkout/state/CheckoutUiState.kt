package com.prayatna.lookiesapp.presentation.checkout.state

import com.prayatna.lookiesapp.domain.model.transaction.ShipmentFee
import com.prayatna.lookiesapp.domain.model.user.UserAddress

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val itemToBuy: CheckoutItemDisplay? = null,
    val checkoutSuccessData: String? = null,
    val existingOrderId: String? = null,
    val selectedMethod: PaymentMethodUiState = PaymentMethodUiState.QRIS,
    val shipmentFees: List<ShipmentFee> = emptyList(),
    val selectedShipmentFee: ShipmentFee? = null,
    val isShipmentLoading: Boolean = false,
    val userAddresses: List<UserAddress> = emptyList(),
    val selectedAddress: UserAddress? = null,
    val isAddressLoading: Boolean = false
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