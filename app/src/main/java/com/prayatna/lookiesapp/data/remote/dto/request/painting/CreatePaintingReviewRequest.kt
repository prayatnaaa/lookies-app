package com.prayatna.lookiesapp.data.remote.dto.request.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaintingReviewRequest(
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("event_painting_id")
    val eventPaintingId: String,
    val rating: Int,
    val review: String? = null
)