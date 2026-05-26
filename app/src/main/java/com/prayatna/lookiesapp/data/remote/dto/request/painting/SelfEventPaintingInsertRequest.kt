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
    @SerialName("artist_business_id")
    val businessId: String,
    val status: String = "accepted"
)