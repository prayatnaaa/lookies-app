package com.prayatna.lookiesapp.presentation.event.detailevent.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.painting.EventPainting

data class DetailEventUiState(
    val isLoading: Boolean = false,
    val info: Event? = null,
    val paintings: List<EventPainting> = emptyList(),
    val detailEventError: String? = null,
    val eventPaintingsError: String? = null
)
