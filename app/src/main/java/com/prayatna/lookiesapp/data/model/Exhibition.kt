package com.prayatna.lookiesapp.data.model


data class Exhibition(
    val eventId: String,
    val paintingId: String,
    val exhibitionPrice: Double,
    val statusInEvent: String,
    val isAuction: Boolean,
    val startingPrice: Double,
    val auctionEndTime: String
)
