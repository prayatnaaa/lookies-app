package com.prayatna.lookiesapp.domain.model.painting

data class PaintingReview(
    val id: String,
    val userId: String,
    val orderId: String,
    val eventPaintingId: String,
    val rating: Int,
    val review: String?,
    val createdAt: String,
)