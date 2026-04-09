package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEvent
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutContent
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutPaymentMethodContent

@Composable
fun CheckoutScreen(
    type: String,
    uiState: CheckoutUiState,
    quantity: Int,
    onEvent: (CheckoutEvent) -> Unit
) {
    val showSuccessDialog = uiState.checkoutSuccessData != null
    val showErrorDialog = uiState.errorMessage != null

    if (showSuccessDialog) {
        CustomBottomSheet(
            title = "Order Created!",
            message = "Continue to payment?",
            confirmText = "Pay now",
            onConfirm = {
                onEvent(CheckoutEvent.OnSuccessConfirmed)
            },
            onDismiss = {}
        )
    }

    if (showErrorDialog) {
        CustomBottomSheet(
            title = "Checkout Failed",
            message = uiState.errorMessage!!,
            confirmText = "OK",
            onConfirm = {
                onEvent(CheckoutEvent.OnErrorConfirmed)
            },
            onDismiss = {
                onEvent(CheckoutEvent.OnErrorConfirmed)
            }
        )
    }

    CheckoutContent(
        type = type,
        uiState = uiState,
        quantity = quantity,

        onBackClick = {
            onEvent(CheckoutEvent.OnBackClick)
        },

        onPayClick = {
            onEvent(CheckoutEvent.OnPayClick)
        },

        onRefresh = {
            onEvent(CheckoutEvent.OnRefresh)
        },

        onShipmentFeeSelected = { fee ->
            onEvent(CheckoutEvent.OnShipmentFeeSelected(fee))
        },

        children = {
            CheckoutPaymentMethodContent(
                selectedMethod = uiState.selectedMethod,
                onPaymentMethodSelected = {
                    onEvent(
                        CheckoutEvent.OnPaymentMethodSelected(it)
                    )
                }
            )
        }
    )
}