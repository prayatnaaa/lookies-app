package com.prayatna.lookiesapp.presentation.offlineCheckout

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutEffect
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun OfflineCheckoutRoute(
    navController: NavController,
    viewModel: OfflineCheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OfflineCheckoutEffect.NavigateBack -> navController.popBackStack()
                is OfflineCheckoutEffect.NavigateToSuccess -> {
                    Toast.makeText(context, "Order Created Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("${NavigationRoutes.DETAIL_TRANSACTION}/${effect.orderId}") {
                        popUpTo(NavigationRoutes.OFFLINE_CHECKOUT) { inclusive = true }
                    }
                }
                is OfflineCheckoutEffect.NavigateToQrisPayment -> {
                    navController.navigate("${NavigationRoutes.QRIS_PAYMENT}/${effect.orderId}/${effect.merchantId}/${effect.amount}") {
                        popUpTo(NavigationRoutes.OFFLINE_CHECKOUT) { inclusive = true }
                    }
                }
                is OfflineCheckoutEffect.NavigateToVaPayment -> {
                    navController.navigate("${NavigationRoutes.VA_PAYMENT}/${effect.orderId}/${effect.merchantId}/${effect.amount}/${effect.bankCode}/${effect.customerName}") {
                        popUpTo(NavigationRoutes.OFFLINE_CHECKOUT) { inclusive = true }
                    }
                }
                is OfflineCheckoutEffect.ShowError -> {
                    // Handled in UI via errorMessage
                }
            }
        }
    }

    OfflineCheckoutScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
