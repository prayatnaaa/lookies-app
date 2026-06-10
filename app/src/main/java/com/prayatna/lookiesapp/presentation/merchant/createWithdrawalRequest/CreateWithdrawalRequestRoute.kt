package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestEffect
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun CreateWithdrawalRequestRoute(
    navController: NavController,
    viewModel: CreateWithdrawalRequestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CreateWithdrawalRequestEffect.NavigateBack -> navController.popBackStack()
                is CreateWithdrawalRequestEffect.ShowMessage -> {
                    // Handled if needed
                }
                is CreateWithdrawalRequestEffect.NavigateToConfirmation -> {
                    navController.navigate("${NavigationRoutes.WITHDRAWAL_CONFIRMATION}/${effect.withdrawalId}") {
                        popUpTo(navController.currentDestination?.id ?: return@navigate) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    if (uiState.errorMessage != null) {
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage!!,
            onConfirm = { viewModel.onEvent(CreateWithdrawalRequestEvent.DismissDialog) },
            onDismiss = { viewModel.onEvent(CreateWithdrawalRequestEvent.DismissDialog) }
        )
    }

    CreateWithdrawalRequestScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
