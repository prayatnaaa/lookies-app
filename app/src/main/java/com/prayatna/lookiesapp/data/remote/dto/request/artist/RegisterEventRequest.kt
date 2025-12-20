package com.prayatna.lookiesapp.data.remote.dto.request.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterEventRequest(
    @SerialName("p_artist_id")
    val artistId: String,
    @SerialName("p_event_id")
    val eventId: Int,
    @SerialName("p_painting_ids")
    val paintingIds: List<Int>
)