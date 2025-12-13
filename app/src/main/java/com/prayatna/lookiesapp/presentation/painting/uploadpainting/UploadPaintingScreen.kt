package com.prayatna.lookiesapp.presentation.painting.uploadpainting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.event.UploadPaintingEvent
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.state.UploadPaintingUiState
import com.prayatna.lookiesapp.presentation.components.artist.UploadPaintingForm

@Composable
fun UploadPaintingScreen(
    navController: NavController,
    viewModel: UploadPaintingViewModel = hiltViewModel()
) {

    val params = viewModel.params
    val image = viewModel.selectedImage
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is UploadPaintingUiState.Success) {
            navController.popBackStack()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onEvent(UploadPaintingEvent.OnImageSelected(uri))
    }

    UploadPaintingForm(
        params = params,
        selectedImage = image,
        uiState = uiState,
        onEvent = {
            if (it is UploadPaintingEvent.OnImageSelected) launcher.launch("image/*")
            else viewModel.onEvent(it)
        }
    )
}
