package com.prayatna.lookiesapp.data.remote.dto.request.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRevenueRulesRequest(
    @SerialName("event_id")
    val eventId: Int,
    @SerialName("item_type")
    val itemType: String,
    @SerialName("artist_percent")
    val artistPercent: Int,
    @SerialName("event_percent")
    val eventPercent: Int,
    @SerialName("platform_percent")
    val platformPercent: Int
)
