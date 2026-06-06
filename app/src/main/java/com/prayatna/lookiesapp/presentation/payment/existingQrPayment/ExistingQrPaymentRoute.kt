package com.prayatna.lookiesapp.presentation.payment.existingQrPayment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun ExistingQrPaymentRoute(
    orderId: String,
    navController: NavController,
    viewModel: ExistingQrPaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.getExistingPaymentAttempt(orderId)
    }

    ExistingQrPaymentScreen(
        state = state,
        onBackClick = { navController.popBackStack() },
        navController = navController
    )
}
