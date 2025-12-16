package com.prayatna.lookiesapp.domain.model.event

data class CreateEventParams(
    val title: String,
    val bannerImageUrl: String,
    val startDate: String,
    val endDate: String,
    val about: String? = null,
    val location: String,
    val locationUrl: String,
    val maxParticipant: Int,
    val maxPainting: Int,
    val maxPaintingPerArtist: Int,
    val ticketPrice: Double,
    val registrationFee: Double
)