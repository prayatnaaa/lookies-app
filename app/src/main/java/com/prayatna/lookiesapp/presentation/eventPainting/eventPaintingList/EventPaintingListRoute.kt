package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun EventPaintingListRoute(
    navController: NavController,
    viewModel: PaintingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventPaintingListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() },
        onPaintingClick = { id ->
            navController.navigate("${NavigationRoutes.DETAIL_EVENT_PAINTING}/$id")
        }
    )
}
