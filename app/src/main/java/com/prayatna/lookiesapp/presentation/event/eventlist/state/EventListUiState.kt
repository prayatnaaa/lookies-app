package com.prayatna.lookiesapp.presentation.event.eventlist.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType

data class EventListUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val searchQuery: String = "",
    val selectedStatus: String? = null,
    val selectedLocation: String? = null,
    val selectedEventType: String? = null,
    val selectedEventFormat: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val isTicketPriceAscending: Boolean = true,
    val errorMessage: String? = null,
    val isFilterSheetOpen: Boolean = false,
    
    // Metadata for filters
    val eventTypes: List<TEventType> = emptyList(),
    val eventFormats: List<EventFormat> = emptyList(),
    val isLoadingMeta: Boolean = false
)
