package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state.SelectPayoutChannelUiEvent

@Composable
fun SelectPayoutChannelRoute(
    navController: NavController,
    viewModel: SelectPayoutChannelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SelectPayoutChannelScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                SelectPayoutChannelUiEvent.OnBackClicked -> navController.popBackStack()
                else -> viewModel.onEvent(event)
            }
        },
        onChannelSelected = { channel ->
            navController.previousBackStackEntry?.savedStateHandle?.set("selected_payout_channel_code", channel.channelCode)
            navController.previousBackStackEntry?.savedStateHandle?.set("selected_payout_channel_name", channel.channelName)
            navController.popBackStack()
        }
    )
}
