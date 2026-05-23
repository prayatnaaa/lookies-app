package com.prayatna.lookiesapp.presentation.painting.editpainting.state

import android.net.Uri

sealed interface EditPaintingUiEvent {
    data class LoadPainting(val id: Int) : EditPaintingUiEvent
    data class TitleChanged(val value: String) : EditPaintingUiEvent
    data class DescriptionChanged(val value: String) : EditPaintingUiEvent
    data class YearChanged(val value: String) : EditPaintingUiEvent
    data class PriceChanged(val value: String) : EditPaintingUiEvent
    data class HeightChanged(val value: String) : EditPaintingUiEvent
    data class WidthChanged(val value: String) : EditPaintingUiEvent
    data class SubjectChanged(val value: String) : EditPaintingUiEvent
    data class ArtStyleChanged(val id: String) : EditPaintingUiEvent
    data class MediumChanged(val id: String) : EditPaintingUiEvent
    data class ImageChanged(val uri: Uri?) : EditPaintingUiEvent
    data object Submit : EditPaintingUiEvent
    data object DismissDialog : EditPaintingUiEvent
}
