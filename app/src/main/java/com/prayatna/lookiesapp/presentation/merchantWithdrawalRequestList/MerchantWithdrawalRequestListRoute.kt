package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEffect

@Composable
fun MerchantWithdrawalRequestListRoute(
    navController: NavController,
    viewModel: MerchantWithdrawalRequestsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MerchantWithdrawalRequestListEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is MerchantWithdrawalRequestListEffect.NavigateToDetail -> {
                    // Navigate to detail if implemented
                }
            }
        }
    }

    MerchantWithdrawalRequestListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
