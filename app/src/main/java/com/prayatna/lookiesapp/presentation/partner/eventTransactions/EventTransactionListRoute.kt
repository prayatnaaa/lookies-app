package com.prayatna.lookiesapp.presentation.partner.eventTransactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListEffect
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListEvent
import com.prayatna.lookiesapp.presentation.partner.orderDetail.navigateToPartnerOrderDetail

@Composable
fun EventTransactionListRoute(
    navController: NavController,
    eventId: String,
    viewModel: EventTransactionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EventTransactionListEffect.NavigateBack -> navController.popBackStack()
                is EventTransactionListEffect.NavigateToOrderDetail -> {
                    navController.navigateToPartnerOrderDetail(effect.orderId)
                }
            }
        }
    }

    EventTransactionListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
