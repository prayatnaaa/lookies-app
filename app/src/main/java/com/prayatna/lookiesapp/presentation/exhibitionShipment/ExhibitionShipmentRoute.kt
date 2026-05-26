package com.prayatna.lookiesapp.presentation.exhibitionShipment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.exhibitionShipment.state.ExhibitionShipmentEvent

@Composable
fun ExhibitionShipmentRoute(
    navController: NavController,
    eventPaintingId: String,
    viewModel: ExhibitionShipmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(eventPaintingId) {
        viewModel.init(eventPaintingId)
    }

    ExhibitionShipmentScreen(
        backAction = { navController.popBackStack() },
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is ExhibitionShipmentEvent.OnBackClick -> navController.popBackStack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}
