package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state

sealed interface UnsoldArtworkReturnEvent {
    data class Init(val eventPaintingId: String) : UnsoldArtworkReturnEvent
    data class OnCourierNameChanged(val name: String) : UnsoldArtworkReturnEvent
    data class OnTrackingNumberChanged(val trackingNumber: String) : UnsoldArtworkReturnEvent
    data class OnNotesChanged(val notes: String) : UnsoldArtworkReturnEvent
    data object SubmitReturnShipment : UnsoldArtworkReturnEvent
    data object ConfirmArtworkReturned : UnsoldArtworkReturnEvent
    data object DismissError : UnsoldArtworkReturnEvent
    data object DismissSuccess : UnsoldArtworkReturnEvent
    data object OnBackClick : UnsoldArtworkReturnEvent
}
