package com.prayatna.lookiesapp.domain.model.painting

data class CreatePaintingReviewInput(
    val orderId: String,
    val eventPaintingId: String,
    val rating: Int,
    val review: String? = null
)