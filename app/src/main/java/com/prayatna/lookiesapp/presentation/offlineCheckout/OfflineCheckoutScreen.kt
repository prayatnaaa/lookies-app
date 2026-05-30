package com.prayatna.lookiesapp.presentation.offlineCheckout

import androidx.compose.runtime.Composable
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutContent
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutPaymentMethodContent
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutEvent
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutUiState

@Composable
fun OfflineCheckoutScreen(
    uiState: OfflineCheckoutUiState,
    onEvent: (OfflineCheckoutEvent) -> Unit
) {
    // Map OfflineCheckoutUiState to CheckoutUiState for CheckoutContent compatibility
    val mappedUiState = CheckoutUiState(
        isLoading = uiState.isLoading,
        itemToBuy = uiState.itemToBuy,
        quantity = uiState.quantity,
        selectedMethod = uiState.selectedMethod,
        selectedBankCode = uiState.selectedBankCode
    )

    if (uiState.errorMessage != null) {
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage,
            confirmText = "OK",
            onConfirm = { onEvent(OfflineCheckoutEvent.DismissError) },
            onDismiss = { onEvent(OfflineCheckoutEvent.DismissError) }
        )
    }

    CheckoutContent(
        type = "offline", 
        uiState = mappedUiState,
        quantity = uiState.quantity,
        onBackClick = { onEvent(OfflineCheckoutEvent.OnBackClick) },
        onPayClick = { onEvent(OfflineCheckoutEvent.OnPayClick) },
        onRefresh = { onEvent(OfflineCheckoutEvent.OnRefresh) },
        onShipmentFeeSelected = {}, 
        onAddressSelected = {}, 
        onAddAddressClick = {}, 
        children = {
            CheckoutPaymentMethodContent(
                selectedMethod = uiState.selectedMethod,
                selectedBankCode = uiState.selectedBankCode,
                onPaymentMethodSelected = {
                    onEvent(OfflineCheckoutEvent.OnPaymentMethodSelected(it))
                },
                onBankCodeSelected = {
                    onEvent(OfflineCheckoutEvent.OnBankCodeSelected(it))
                }
            )
        }
    )
}
