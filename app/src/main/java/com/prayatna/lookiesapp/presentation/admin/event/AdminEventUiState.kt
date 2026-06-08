package com.prayatna.lookiesapp.presentation.admin.event

import com.prayatna.lookiesapp.domain.model.event.Event

data class AdminEventUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
    val title: String = "",
    val status: EventStatus? = null,
)

enum class EventStatus(val type: String) {
    PENDING("pending_validation"),
    PUBLISHED("published"),
    UPCOMING("upcoming"),
    COMPLETED("finished"),
    CANCELLED("cancelled"),
    REJECTED("rejected");
}
