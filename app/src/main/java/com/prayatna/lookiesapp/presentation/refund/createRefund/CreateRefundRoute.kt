package com.prayatna.lookiesapp.presentation.refund.createRefund

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.navigateToSelectPayoutChannel
import com.prayatna.lookiesapp.presentation.refund.createRefund.state.CreateRefundEvent

@Composable
fun CreateRefundRoute(
    navController: NavController,
    orderId: String,
    viewModel: CreateRefundViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val selectedCode = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_payout_channel_code")
    val selectedName = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_payout_channel_name")

    LaunchedEffect(selectedCode, selectedName) {
        if (selectedCode != null && selectedName != null) {
            viewModel.onEvent(CreateRefundEvent.BankCodeChanged(selectedCode))
            viewModel.onEvent(CreateRefundEvent.BankNameChanged(selectedName))
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_code")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_name")
        }
    }

    CreateRefundScreen(
        orderId = orderId,
        uiState = uiState,
        formState = formState,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() },
        onSelectBankClick = { navController.navigateToSelectPayoutChannel() }
    )
}
