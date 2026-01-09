package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting

data class EventPaintingDetailUiState (
    val errorMessage: String? = null,
    val data: EventPainting? = null,
    val isLoading: Boolean = false
)