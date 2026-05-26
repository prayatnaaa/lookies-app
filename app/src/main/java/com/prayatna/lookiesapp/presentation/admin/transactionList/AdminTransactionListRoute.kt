package com.prayatna.lookiesapp.presentation.admin.transactionList

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiEffect

@Composable
fun AdminTransactionListRoute(
    navController: NavController,
    viewModel: AdminTransactionListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                AdminTransactionListUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is AdminTransactionListUiEffect.NavigateToDetail -> {
                    //TODO: create detail transaction for admin
                }
                is AdminTransactionListUiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    AdminTransactionListScreen(
        uiState = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}