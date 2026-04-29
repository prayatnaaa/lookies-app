package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.painting.CreatePaintingReviewRequest
import com.prayatna.lookiesapp.domain.model.painting.CreatePaintingReviewInput

fun CreatePaintingReviewInput.toDto() =
    CreatePaintingReviewRequest(
        orderId = orderId,
        eventPaintingId = eventPaintingId,
        rating = rating,
        review = review,
    )