package com.prayatna.lookiesapp.data.remote.dto.request.painting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelfEventPaintingInsertRequest(
    @SerialName("event_id")
    val eventId: String,
    @SerialName("painting_id")
    val paintingId: Int,
    @SerialName("final_price")
    val finalPrice: Double,
    val status: String = "approved"
)