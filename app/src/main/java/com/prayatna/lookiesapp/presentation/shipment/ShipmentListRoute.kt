package com.prayatna.lookiesapp.presentation.shipment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiEvent

@Composable
fun ShipmentListRoute(
    navController: NavController,
    viewModel: ShipmentListViewModel = hiltViewModel(),
    merchantId: String
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(merchantId) {
        viewModel.onEvent(ShipmentListUiEvent.LoadShipments(merchantId))
    }

    ShipmentListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackPress = { navController.popBackStack() },
        onShipmentCardClick = { orderId ->
            navController.navigate("${com.prayatna.lookiesapp.utils.NavigationRoutes.SHIPMENT_DETAIL}/$orderId")
        }
    )

}