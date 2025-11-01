package com.prayatna.lookiesapp.presentation.event.detailevent

import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo

data class DetailEventUiState (
    val isLoading: Boolean = false,
    val info: DetailEventInfo? = null,
    val errorMessage: String? = null
)