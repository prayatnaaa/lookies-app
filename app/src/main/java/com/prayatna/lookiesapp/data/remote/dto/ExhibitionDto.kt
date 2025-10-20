package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExhibitionDto(
    @SerialName("event_id") val eventId: String,
    @SerialName("painting_id") val paintingId: String,
    @SerialName("exhibition_price") val exhibitionPrice: Double,
    @SerialName("status_in_event") val statusInEvent: String,
    @SerialName("is_auction") val isAuction: Boolean,
    @SerialName("starting_price") val startingPrice: Double,
    @SerialName("auction_end_time") val auctionEndTime: String
)
