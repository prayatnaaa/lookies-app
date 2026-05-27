package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state

sealed interface EventPaintingGalleryUiEffect {
    data object NavigateBack : EventPaintingGalleryUiEffect
    data class NavigateToDetail(val paintingId: String) : EventPaintingGalleryUiEffect
}
