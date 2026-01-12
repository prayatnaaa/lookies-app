package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting

data class EventPaintingListUiState(
    val isLoading: Boolean = false,
    val paintings: List<EventPainting> = emptyList(),
    val filteredPaintings: List<EventPainting> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)