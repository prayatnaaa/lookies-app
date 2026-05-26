package com.prayatna.lookiesapp.data.remote.dto

import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetPaintingDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventPaintingDto(
    @SerialName("id")
    val id: String,

    @SerialName("artist_user_id")
    val artistId: String,

    @SerialName("event_id")
    val eventId: String,

    @SerialName("final_price")
    val finalPrice: Double,

    @SerialName("status")
    val status: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null,

    @SerialName("paintings")
    val painting: GetPaintingDto,

    @SerialName("event_participants")
    val participant: EventParticipantDto
)