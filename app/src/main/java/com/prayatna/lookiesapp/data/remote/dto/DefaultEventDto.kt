package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DefaultEventDto(
    val id: String,
    val title: String,
    @SerialName("organizer_id")
    val organizerId: String,
    @SerialName("banner_image_url")
    val bannerImageUrl: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val about: String? = null,
    val location: String,
    @SerialName("location_url")
    val locationUrl: String,
    @SerialName("max_participant")
    val maxParticipant: Int? = null,
    @SerialName("max_painting")
    val maxPainting: Int? = null,
    @SerialName("max_painting_per_artist")
    val maxPaintingPerArtist: Int? = null,
    val status: String,
    @SerialName("ticket_price")
    val ticketPrice: Double? = null,
    @SerialName("registration_fee")
    val artistRegistrationFee: Double? = null,
    @SerialName("event_type_id")
    val eventType: String,
    @SerialName("event_format_id")
    val eventFormat: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String? = null
)