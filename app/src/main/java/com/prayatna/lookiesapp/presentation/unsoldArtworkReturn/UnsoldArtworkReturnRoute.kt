package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state.UnsoldArtworkReturnEvent

@Composable
fun UnsoldArtworkReturnRoute(
    navController: NavController,
    eventPaintingId: String,
    viewModel: UnsoldArtworkReturnViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UnsoldArtworkReturnScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                UnsoldArtworkReturnEvent.OnBackClick -> navController.popBackStack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}
