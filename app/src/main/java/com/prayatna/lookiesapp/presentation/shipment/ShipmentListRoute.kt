package com.prayatna.lookiesapp.presentation.shipment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun ShipmentListRoute(
    navController: NavController,
    viewModel: ShipmentListViewModel = hiltViewModel(),
    merchantId: String
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(merchantId) {
        viewModel.getShipmentsByMerchantId(merchantId)
    }

    ShipmentListScreen(
        uiState = uiState,
        onBackPress = { navController.popBackStack() },
        onShipmentCardClick = { shipmentId ->
            //Todo: Navigate to shipment detail screen
        }
    )

}