package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEffect
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun CheckoutRoute(
    type: String,
    quantity: Int,
    itemId: String,
    navController: NavController,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // ✅ Initial load
    LaunchedEffect(Unit) {
        viewModel.onEvent(
            CheckoutEvent.OnLoad(type, itemId, quantity)
        )
    }

    // ✅ ONLY navigation handled here
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                CheckoutEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                CheckoutEffect.NavigateToAddAddress -> {
                    navController.navigate(NavigationRoutes.CREATE_USER_ADDRESS)
                }

                is CheckoutEffect.NavigateToQrisPayment -> {
                    navController.navigate(
                        "${NavigationRoutes.QRIS_PAYMENT}/${effect.orderId}/${effect.merchantId}/${effect.amount}"
                    )
                }

                else -> Unit // ❗ jangan handle dialog/snackbar di sini
            }
        }
    }

    CheckoutScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        effectFlow = viewModel.effect,
        quantity = quantity,
        type = type,
    )
}