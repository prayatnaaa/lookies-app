package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting

data class EventPaintingGalleryUiState(
    val isLoading: Boolean = false,
    val paintings: List<EventPainting> = emptyList(),
    val errorMessage: String? = null,
    val eventId: String = ""
)
