package com.prayatna.lookiesapp.presentation.painting.uploadpainting.event

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute

sealed class UploadPaintingEvent {
    data class OnTitleChange(val value: String) : UploadPaintingEvent()
    data class OnDescriptionChange(val value: String) : UploadPaintingEvent()
    data class OnHeightChange(val value: String) : UploadPaintingEvent()
    data class OnWidthChange(val value: String) : UploadPaintingEvent()
    data class OnMediumChange(val value: PaintingAttribute) : UploadPaintingEvent()
    data class OnArtStyleChange(val value: PaintingAttribute) : UploadPaintingEvent()
    data class OnSubjectChange(val value: String) : UploadPaintingEvent()
    data class OnYearChange(val value: String) : UploadPaintingEvent()

    data class OnImageSelected(val uri: Uri?) : UploadPaintingEvent()

    data object ValidateAndSubmit : UploadPaintingEvent()
    data object DismissDialog : UploadPaintingEvent()
}
