package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state

sealed interface EventPaintingGalleryUiEvent {
    data class LoadPaintings(val eventId: String) : EventPaintingGalleryUiEvent
    data class OnPaintingClicked(val paintingId: String) : EventPaintingGalleryUiEvent
    data object OnBackClicked : EventPaintingGalleryUiEvent
}
