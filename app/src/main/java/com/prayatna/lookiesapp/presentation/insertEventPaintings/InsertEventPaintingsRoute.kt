package com.prayatna.lookiesapp.presentation.insertEventPaintings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun InsertEventPaintingsRoute(
    navController: NavController,
    viewModel: InsertEventPaintingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.popBackStack()
        }
    }

    InsertEventPaintingsScreen(
        uiState = uiState,
        onTogglePainting = viewModel::togglePainting,
        onSubmit = viewModel::submitSelectedPaintings,
        onBackClick = {
            navController.popBackStack()
        }
    )
}