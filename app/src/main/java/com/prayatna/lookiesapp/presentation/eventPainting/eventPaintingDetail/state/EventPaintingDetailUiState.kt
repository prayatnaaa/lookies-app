package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview

data class EventPaintingDetailUiState (
    val errorMessage: String? = null,
    val data: EventPainting? = null,
    val paintingReview: PaintingReview? = null,
    val isLoading: Boolean = false,
    val actionLoading: Boolean = false
)