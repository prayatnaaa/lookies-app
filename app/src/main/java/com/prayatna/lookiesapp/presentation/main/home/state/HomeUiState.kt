package com.prayatna.lookiesapp.presentation.main.home.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.user.Profile

data class HomeUiState(
    val isLoadingEvents: Boolean = false,
    val isLoadingPaintings: Boolean = false,
    val isRefreshing: Boolean = false,

    val events: List<Event> = emptyList(),
    val eventPaintings: List<EventPainting> = emptyList(),
    val user: Profile? = null,

    val errorMessage: String? = null
)