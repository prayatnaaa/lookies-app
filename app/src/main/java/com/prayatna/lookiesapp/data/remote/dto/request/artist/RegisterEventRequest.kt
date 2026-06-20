package com.prayatna.lookiesapp.data.remote.dto.request.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterEventRequest(
    @SerialName("artist_id")
    val artistId: String,
    @SerialName("event_id")
    val eventId: Int,
    @SerialName("painting_ids")
    val paintingIds: List<Int>,
    @SerialName("comission_rate")
    val commissionRate: Int
)
