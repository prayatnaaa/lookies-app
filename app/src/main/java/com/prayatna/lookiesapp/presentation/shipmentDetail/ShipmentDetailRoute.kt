package com.prayatna.lookiesapp.presentation.shipmentDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailEvent

@Composable
fun ShipmentDetailRoute(
    navController: NavController,
    orderId: String,
    viewModel: ShipmentDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.getShipment(orderId)
    }

    ShipmentDetailScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is ShipmentDetailEvent.OnBackClick -> navController.popBackStack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}
