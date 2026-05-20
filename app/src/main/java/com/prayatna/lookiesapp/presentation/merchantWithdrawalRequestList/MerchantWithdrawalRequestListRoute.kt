package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEffect
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MerchantWithdrawalRequestListRoute(
    navController: NavController,
    viewModel: MerchantWithdrawalRequestsListViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        savedStateHandle
            ?.getStateFlow("withdrawal_created", false)
            ?.collect { created ->

                if (created) {
                    viewModel.onEvent(
                        MerchantWithdrawalRequestListEvent.ShowCreatedSnackBar
                    )

                    viewModel.onEvent(
                        MerchantWithdrawalRequestListEvent.Refresh
                    )

                    savedStateHandle["withdrawal_created"] = false
                }
            }
    }


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MerchantWithdrawalRequestListEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is MerchantWithdrawalRequestListEffect.NavigateToDetail -> {
                    // Navigate to detail if implemented
                }

                is MerchantWithdrawalRequestListEffect.NavigateCreateWithdrawal -> {
                    navController.navigate("${NavigationRoutes.CREATE_WITHDRAWAL_REQUEST}/${effect.id}")
                }

                is MerchantWithdrawalRequestListEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    MerchantWithdrawalRequestListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent
    )
}
