package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun EventPaintingGalleryRoute(
    navController: NavController,
    eventId: String,
    viewModel: EventPaintingGalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EventPaintingGalleryUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is EventPaintingGalleryUiEffect.NavigateToDetail -> {
                    navController.navigate("${NavigationRoutes.DETAIL_EVENT_PAINTING}/${effect.paintingId}")
                }
            }
        }
    }

    EventPaintingGalleryScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
