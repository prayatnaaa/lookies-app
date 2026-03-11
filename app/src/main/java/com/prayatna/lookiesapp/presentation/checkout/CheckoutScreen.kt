package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.presentation.components.CustomDialog
import com.prayatna.lookiesapp.presentation.components.checkout.CheckoutContent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun CheckoutScreen(
    type: String,
    quantity: Int = 1,
    itemId: String,
    viewModel: CheckoutViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showSuccessDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getDetailItem(type = type, id = itemId)
    }

    LaunchedEffect(uiState.checkoutSuccessData, uiState.errorMessage) {
        if (uiState.checkoutSuccessData != null) {
            showSuccessDialog.value = true
        }
        if (uiState.errorMessage != null) {
            showErrorDialog.value = true
        }
    }

    if (showSuccessDialog.value) {
        CustomDialog(
            title = "Order Created!",
            message = "Continue to payment?",
            confirmText = "Pay now",
            onConfirm = {
                showSuccessDialog.value = false

                val orderId = uiState.checkoutSuccessData
                val merchantId = uiState.itemToBuy?.merchantId

                val price = uiState.itemToBuy?.price
                val totalAmount = price?.times(quantity)

                viewModel.onCheckoutResultConsumed()

                navController.navigate(
                    "${NavigationRoutes.PAYMENT}/$orderId/$merchantId/$totalAmount"
                ) {
                    popUpTo("checkout_route") { inclusive = true }
                }
            },
            onDismiss = {
                showSuccessDialog.value = false
                viewModel.onCheckoutResultConsumed()
                navController.popBackStack()
            }
        )
    }

    if (showErrorDialog.value) {
        CustomDialog(
            title = "Checkout Failed",
            message = uiState.errorMessage ?: "Terjadi kesalahan.",
            confirmText = "OK",
            onConfirm = {
                showErrorDialog.value = false
                viewModel.onCheckoutResultConsumed()
            },
            onDismiss = {
                showErrorDialog.value = false
                viewModel.onCheckoutResultConsumed()
            }
        )
    }

    CheckoutContent(
        uiState = uiState,
        onBackClick = {
            navController.popBackStack()
        },
        quantity = quantity,
        onPayClick = {
            val currentItem = uiState.itemToBuy
            if (currentItem != null) {
                val orderItem = OrderItemInput(
                    itemType = currentItem.type,
                    itemRefId = currentItem.id,
                    quantity = quantity
                )
                viewModel.createCheckout(
                    items = listOf(orderItem)
                )
            }
        },
        onRefresh = {
            viewModel.getDetailItem(type, itemId)
        }
    )
}