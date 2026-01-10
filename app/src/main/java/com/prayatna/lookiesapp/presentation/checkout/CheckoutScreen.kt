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

@Composable
fun CheckoutScreen(
    type: String,
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
            message = "Pesanan berhasil dibuat. Lanjutkan ke pembayaran?",
            confirmText = "Bayar Sekarang",
            onConfirm = {
                showSuccessDialog.value = false
                viewModel.onCheckoutResultConsumed()

//              todo: create navigation to payment gateway
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
        onPayClick = {
            val currentItem = uiState.itemToBuy
            if (currentItem != null) {
                val orderItem = OrderItemInput(
                    itemType = currentItem.type,
                    itemRefId = currentItem.id,
                    quantity = 1
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