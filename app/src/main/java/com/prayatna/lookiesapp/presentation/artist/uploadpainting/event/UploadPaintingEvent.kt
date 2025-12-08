package com.prayatna.lookiesapp.presentation.artist.uploadpainting.event

import android.net.Uri

sealed class UploadPaintingEvent {
    data class OnTitleChange(val value: String) : UploadPaintingEvent()
    data class OnDescriptionChange(val value: String) : UploadPaintingEvent()
    data class OnHeightChange(val value: String) : UploadPaintingEvent()
    data class OnWidthChange(val value: String) : UploadPaintingEvent()
    data class OnMediumChange(val value: String) : UploadPaintingEvent()
    data class OnArtStyleChange(val value: String) : UploadPaintingEvent()
    data class OnSubjectChange(val value: String) : UploadPaintingEvent()
    data class OnYearChange(val value: String) : UploadPaintingEvent()

    data class OnImageSelected(val uri: Uri?) : UploadPaintingEvent()

    data object ValidateAndSubmit : UploadPaintingEvent()
    data object DismissDialog : UploadPaintingEvent()
}
