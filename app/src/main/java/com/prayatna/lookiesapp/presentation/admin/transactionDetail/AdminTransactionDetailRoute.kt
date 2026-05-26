package com.prayatna.lookiesapp.presentation.admin.transactionDetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiEffect
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiEvent

@Composable
fun AdminTransactionDetailRoute(
    navController: NavController,
    orderId: String,
    viewModel: AdminTransactionDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.onEvent(AdminTransactionDetailUiEvent.LoadTransactionDetail(orderId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AdminTransactionDetailUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is AdminTransactionDetailUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    AdminTransactionDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
