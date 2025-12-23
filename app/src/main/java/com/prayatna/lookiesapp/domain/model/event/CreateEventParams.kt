package com.prayatna.lookiesapp.domain.model.event

data class CreateEventParams(
    val title: String,
    val bannerImageUrl: String,
    val startDate: String,
    val endDate: String,
    val about: String? = null,
    val location: String,
    val locationUrl: String,
    val maxParticipant: Int? = null,
    val maxPainting: Int? = null,
    val maxPaintingPerArtist: Int? = null,
    val ticketPrice: Double? = null,
    val registrationFee: Double? = null,
    val eventType: Int,
    val eventFormat: Int
)