package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaintingReviewDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("event_painting_id")
    val eventPaintingId: String,
    val rating: Int,
    val review: String?,
    @SerialName("created_at")
    val createdAt: String,
)