package com.prayatna.lookiesapp.presentation.partner.selfEventList.state

import com.prayatna.lookiesapp.domain.model.event.Event

data class SelfEventListUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedStatus: String? = null
)
