package com.prayatna.lookiesapp.data.model

data class Event (
    val organizerId: String,
    val title: String,
    val bannerImageUrl: String?,
    val location: String,
    val ticketPrice: Double,
    val registrationFee: Double,
    val date: String,
)
