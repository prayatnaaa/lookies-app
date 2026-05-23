package com.prayatna.lookiesapp.presentation.painting.editpainting

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiEffect
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiEvent

@Composable
fun EditPaintingRoute(
    navController: NavController,
    paintingId: Int,
    viewModel: EditPaintingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(paintingId) {
        viewModel.onEvent(EditPaintingUiEvent.LoadPainting(paintingId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is EditPaintingUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                EditPaintingUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    EditPaintingScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() }
    )
}
