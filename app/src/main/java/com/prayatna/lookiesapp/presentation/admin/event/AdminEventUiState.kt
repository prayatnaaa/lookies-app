package com.prayatna.lookiesapp.presentation.admin.event

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType

data class AdminEventUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
    val title: String = "",
    val status: EventStatus? = null,
    val selectedType: TEventType? = null,
    val selectedFormat: EventFormat? = null,
    val eventTypes: List<TEventType> = emptyList(),
    val eventFormats: List<EventFormat> = emptyList(),
    val isLoadingMeta: Boolean = false
)

enum class EventStatus(val type: String) {
    PENDING("pending_validation"),
    PUBLISHED("published"),
    UPCOMING("upcoming"),
    COMPLETED("finished"),
    CANCELLED("cancelled"),
    REJECTED("rejected");
}
