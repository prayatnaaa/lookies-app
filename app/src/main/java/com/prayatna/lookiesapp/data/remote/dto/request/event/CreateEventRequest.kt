package com.prayatna.lookiesapp.data.remote.dto.request.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    @SerialName("organizer_id")
    val organizerId: String,
    val title: String,
    @SerialName("banner_image_url")
    val bannerImageUrl: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("painting_submission_deadline")
    val paintingSubmissionDeadline: String?,
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
    @SerialName("ticket_price")
    val ticketPrice: Double? = null,
    @SerialName("registration_fee")
    val registrationFee: Double? = null,
    @SerialName("event_type_id")
    val eventType: Int,
    @SerialName("event_format_id")
    val eventFormat: Int,
    @SerialName("painting_artist_percent")
    val paintingArtistPercent: Int,
    @SerialName("painting_event_percent")
    val paintingEventPercent: Int,
    @SerialName("painting_platform_percent")
    val paintingPlatformPercent: Int? = null,
    @SerialName("ticket_artist_percent")
    val ticketArtistPercent: Int? = null,
    @SerialName("ticket_event_percent")
    val ticketEventPercent: Int? = null,
    @SerialName("ticket_platform_percent")
    val ticketPlatformPercent: Int? = null,
)

