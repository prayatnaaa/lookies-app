package com.prayatna.lookiesapp.data.remote.dto.request.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    val title: String,
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
    val maxParticipant: Int,
    @SerialName("max_painting")
    val maxPainting: Int,
    @SerialName("max_painting_per_artist")
    val maxPaintingPerArtist: Int,
    @SerialName("ticket_price")
    val ticketPrice: Double,
    @SerialName("registration_fee")
    val registrationFee: Double,
    @SerialName("event_type_id")
    val eventType: Int,
    @SerialName("event_format_id")
    val eventFormat: Int
)

