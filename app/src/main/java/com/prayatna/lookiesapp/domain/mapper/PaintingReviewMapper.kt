package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.PaintingReviewDto
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview

fun PaintingReviewDto.toDomain() = PaintingReview(
    id = id,
    userId = userId,
    orderId = orderId,
    eventPaintingId = eventPaintingId,
    rating = rating,
    review = review,
    createdAt = createdAt
)