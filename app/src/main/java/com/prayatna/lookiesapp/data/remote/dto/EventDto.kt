package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto (

    val id: Int?,

    @SerialName("organizer_id")
    val organizerId: String,

    val title: String,

    @SerialName("banner_image_url")
    val bannerImageUrl: String?,

    val location: String,

    @SerialName("ticket_price")
    val ticketPrice: Double,

    @SerialName("registration_fee")
    val registrationFee: Double,

    val date: String,

    val status: String?
)
