package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("id")
    val eventId: String,

    val title: String,

    @SerialName("banner_image_url")
    val bannerImageUrl: String,

    @SerialName("remaining_participant_quota")
    val remainingParticipantQuota: Int? = null,

    @SerialName("remaining_painting_quota")
    val remainingPaintingQuota: Int? = null,

    val status: String,

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

    @SerialName("ticket_price")
    val ticketPrice: Double? = null,

    @SerialName("registration_fee")
    val registrationFee: Double? = null,

    @SerialName("painting_submission_deadline")
    val paintingSubmissionDeadline: String? = null,

    @SerialName("registration_start_date")
    val artistRegistrationStartDate: String? = null,

    @SerialName("registration_end_date")
    val artistRegistrationEndDate: String? = null,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    val organizer: MerchantBusinessDto,

    @SerialName("event_type")
    val eventType: EventTypeDto,

    @SerialName("event_format")
    val eventFormat: EventFormatDto,

    @SerialName("approved_at")
    val approvedAt: String? = null,

    @SerialName("approved_by")
    val approvedBy: String? = null,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null
)
