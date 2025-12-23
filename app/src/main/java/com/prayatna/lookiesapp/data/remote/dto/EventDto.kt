package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
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
    val maxParticipant: Int? = null,
    @SerialName("max_painting")
    val maxPainting: Int? = null,
    @SerialName("max_painting_per_artist")
    val maxPaintingPerArtist: Int? = null,
    val status: String,
    @SerialName("ticket_price")
    val ticketPrice: Int? = null,
    @SerialName("registration_fee")
    val artistRegistrationFee: Int? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
