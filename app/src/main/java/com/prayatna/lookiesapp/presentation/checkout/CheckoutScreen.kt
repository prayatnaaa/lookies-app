package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
            title = "Success",
            message = "Checkout berhasil 🎉",
            confirmText = "OK",
            onConfirm = {
                showSuccessDialog.value = false
                viewModel.onCheckoutResultConsumed()
                navController.popBackStack()
            },
            onDismiss = {
                showSuccessDialog.value = false
                viewModel.onCheckoutResultConsumed()
            }
        )
    }

    if (showErrorDialog.value) {
        CustomDialog(
            title = "Error",
            message = uiState.errorMessage.orEmpty(),
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
        onPayClick = { description ->
            val currentItem = uiState.itemToBuy
            if (currentItem != null) {
                viewModel.createCheckout(
                    totalAmount = currentItem.price ?: 0.0,
                    type = type,
                    description = description,
                )
            }
        },
        onRefresh = {
            viewModel.getDetailItem(type, itemId)
        }
    )
}
