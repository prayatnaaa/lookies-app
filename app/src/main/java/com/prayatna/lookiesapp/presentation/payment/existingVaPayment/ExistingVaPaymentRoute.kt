package com.prayatna.lookiesapp.presentation.payment.existingVaPayment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun ExistingVaPaymentRoute(
    orderId: String,
    navController: NavController,
    viewModel: ExistingVaPaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.getExistingPaymentAttempt(orderId)
    }

    ExistingVaPaymentScreen(
        state = state,
        onBackClick = { navController.popBackStack() },
        navController = navController
    )
}
