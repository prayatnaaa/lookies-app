package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiEffect
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiEvent

@Composable
fun AdminWithdrawalDetailRoute(
    withdrawalId: String,
    onNavigateBack: () -> Unit,
    viewModel: AdminWithdrawalDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(withdrawalId) {
        viewModel.onEvent(AdminWithdrawalDetailUiEvent.LoadDetail(withdrawalId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AdminWithdrawalDetailUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                AdminWithdrawalDetailUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    AdminWithdrawalDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}
