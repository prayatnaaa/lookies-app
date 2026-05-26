package com.prayatna.lookiesapp.presentation.refund.refundDetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailEffect
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailEvent

@Composable
fun RefundDetailRoute(
    navController: NavController,
    refundId: String,
    viewModel: RefundDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(refundId) {
        viewModel.onEvent(RefundDetailEvent.LoadRefund(refundId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RefundDetailEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                RefundDetailEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    RefundDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() }
    )
}
