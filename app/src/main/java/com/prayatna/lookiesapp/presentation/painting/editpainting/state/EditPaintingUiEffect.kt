package com.prayatna.lookiesapp.presentation.painting.editpainting.state

sealed interface EditPaintingUiEffect {
    data object NavigateBack : EditPaintingUiEffect
    data class ShowToast(val message: String) : EditPaintingUiEffect
}
