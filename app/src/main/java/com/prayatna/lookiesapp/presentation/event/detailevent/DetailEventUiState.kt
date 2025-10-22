package com.prayatna.lookiesapp.presentation.event.detailevent

import com.prayatna.lookiesapp.data.model.DetailEventInfo

data class DetailEventUiState (
    val isLoading: Boolean = false,
    val info: DetailEventInfo? = null,
    val errorMessage: String? = null
)