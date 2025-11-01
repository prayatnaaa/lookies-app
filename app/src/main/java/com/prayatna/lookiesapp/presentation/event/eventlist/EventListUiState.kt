package com.prayatna.lookiesapp.presentation.event.eventlist

import com.prayatna.lookiesapp.domain.model.event.Event

data class EventListUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
)