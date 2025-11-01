package com.prayatna.lookiesapp.presentation.admin.event

import com.prayatna.lookiesapp.domain.model.event.Event

data class AdminEventUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
)