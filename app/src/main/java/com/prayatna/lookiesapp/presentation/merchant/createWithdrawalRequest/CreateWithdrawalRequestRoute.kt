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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("withdrawal_created", true)

                    navController.popBackStack()
                }
            }
        }
    }

    if (uiState.successMessage != null) {
        CustomBottomSheet(
            title = "Success",
            message = uiState.successMessage!!,
            onConfirm = {
                viewModel.onEvent(CreateWithdrawalRequestEvent.DismissDialog)
                navController.popBackStack()
            },
            onDismiss = {
                viewModel.onEvent(CreateWithdrawalRequestEvent.DismissDialog)
                navController.popBackStack()
            }
        )
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
