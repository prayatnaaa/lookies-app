package com.prayatna.lookiesapp.domain.model.event

data class Event (
    val id: String,
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
    val ticketPrice: Int? = null,
    val artistRegistrationFee: Int? = null,
    val status: String,
    val createdAt: String,
    val updatedAt: String? = null
)
