package com.prayatna.lookiesapp.presentation.partner.selfEventList.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType

data class SelfEventListUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedStatus: String? = null,
    val selectedFormatId: Int? = null,
    val selectedTypeId: Int? = null,
    val formats: List<EventFormat> = emptyList(),
    val types: List<TEventType> = emptyList(),
    val isFilterSheetOpen: Boolean = false
)
