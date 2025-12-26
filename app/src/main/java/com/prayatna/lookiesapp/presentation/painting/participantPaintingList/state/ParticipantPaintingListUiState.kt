package com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting


data class ParticipantPaintingListUiState(
    val isLoading: Boolean = false,
    val eventPaintings: List<EventPainting> = emptyList(),
    val errorMessage: String? = null,
)
