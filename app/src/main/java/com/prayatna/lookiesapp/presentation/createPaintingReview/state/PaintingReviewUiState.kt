package com.prayatna.lookiesapp.presentation.createPaintingReview.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview

data class PaintingReviewUiState(
    val isLoading: Boolean = false,
    val reviewNotes: String = "",
    val reviewStarRating: Int = 0,
    val error: String? = null,
    val data: PaintingReview? = null,
    val eventPainting: EventPainting? = null
)