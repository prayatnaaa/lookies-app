package com.prayatna.lookiesapp.presentation.admin.detailEvent.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules

data class AdminDetailEventUiState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val revenueRules: List<EventRevenueRules> = emptyList(),
    val errorMessage: String? = null,
    val isDeciding: Boolean = false
)
