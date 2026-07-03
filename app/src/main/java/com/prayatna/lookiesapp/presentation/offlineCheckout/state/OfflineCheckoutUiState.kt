package com.prayatna.lookiesapp.presentation.offlineCheckout.state

import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutItemDisplay
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState

data class OfflineCheckoutUiState(
    val isLoading: Boolean = false,
    val type: String = "painting", 
    val itemId: String = "",
    val quantity: Int = 1,
    val itemToBuy: CheckoutItemDisplay? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val createdOrderId: String? = null,

    val selectedMethod: PaymentMethodUiState = PaymentMethodUiState.QRIS,
    val selectedBankCode: String = "BRI"
)
