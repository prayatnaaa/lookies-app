package com.prayatna.lookiesapp.presentation.partner.orderDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.partner.orderDetail.state.PartnerOrderDetailEffect
import com.prayatna.lookiesapp.presentation.partner.orderDetail.state.PartnerOrderDetailEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerOrderDetailRoute(
    navController: NavController,
    orderId: String,
    viewModel: PartnerOrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.onEvent(PartnerOrderDetailEvent.LoadOrderDetail(orderId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PartnerOrderDetailEffect.NavigateBack -> navController.popBackStack()
                is PartnerOrderDetailEffect.NavigateToArtworkDetail -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            }
        }
    }

    PartnerOrderDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
