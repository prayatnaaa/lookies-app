package com.prayatna.lookiesapp.presentation.painting.uploadpainting.state

sealed class UploadPaintingUiState {
    data object Idle : UploadPaintingUiState()
    data object Loading : UploadPaintingUiState()
    data class Success(val message: String = "Upload Success!") : UploadPaintingUiState()
    data class Error(val message: String) : UploadPaintingUiState()
}
