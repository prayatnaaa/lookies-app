package com.prayatna.lookiesapp.presentation.event.detailevent

import com.prayatna.lookiesapp.domain.model.event.Event

data class DetailEventUiState (
    val isLoading: Boolean = false,
    val info: Event? = null,
    val errorMessage: String? = null
)