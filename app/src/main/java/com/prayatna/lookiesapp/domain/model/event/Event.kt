package com.prayatna.lookiesapp.domain.model.event

data class Event (
    val id: String,
    val title: String,
    val organizerId: String,
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
    val artistRegistrationFee: Double? = null,
    val eventType: String,
    val eventFormat: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String? = null
)
