package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEffect
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEvent
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutContent
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutPaymentMethodContent
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun CheckoutScreen(
    type: String,
    quantity: Int,
    uiState: CheckoutUiState,
    onEvent: (CheckoutEvent) -> Unit,
    effectFlow: SharedFlow<CheckoutEffect>,
) {

    val showSuccessDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {

                is CheckoutEffect.ShowErrorDialog -> {
                    errorMessage.value = effect.message
                }

                CheckoutEffect.ShowSuccessDialog -> {
                    showSuccessDialog.value = true
                }

                else -> Unit
            }
        }
    }

    // ✅ SUCCESS DIALOG
    if (showSuccessDialog.value) {
        CustomBottomSheet(
            title = "Order Created!",
            message = "Continue to payment?",
            confirmText = "Pay now",
            onConfirm = {
                showSuccessDialog.value = false
                onEvent(CheckoutEvent.OnPayClick) // lanjut payment
            },
            onDismiss = {
                showSuccessDialog.value = false
            }
        )
    }

    // ✅ ERROR DIALOG
    errorMessage.value?.let { message ->
        CustomBottomSheet(
            title = "Checkout Failed",
            message = message,
            confirmText = "OK",
            onConfirm = {
                errorMessage.value = null
            },
            onDismiss = {
                errorMessage.value = null
            }
        )
    }

    // CONTENT
    CheckoutContent(
        uiState = uiState,

        onBackClick = { onEvent(CheckoutEvent.OnBackClick) },
        onPayClick = { onEvent(CheckoutEvent.OnPayClick) },
        onRefresh = { onEvent(CheckoutEvent.OnRefresh) },

        onShipmentFeeSelected = {
            onEvent(CheckoutEvent.OnShipmentFeeSelected(it))
        },

        onAddressSelected = {
            onEvent(CheckoutEvent.OnAddressSelected(it))
        },

        onAddAddressClick = {
            onEvent(CheckoutEvent.OnAddAddressClick)
        },

        children = {
            CheckoutPaymentMethodContent(
                selectedMethod = uiState.selectedMethod,
                onPaymentMethodSelected = {
                    onEvent(CheckoutEvent.OnPaymentMethodSelected(it))
                }
            )
        },
        type = type ,
        quantity = quantity,
    )
}