package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventRevenueRulesDto(
    val id: String,
    @SerialName("event_id")
    val eventId: Int,
    @SerialName("item_type")
    val itemType: String,
    @SerialName("artist_percent")
    val artistPercent: Int,
    @SerialName("event_percent")
    val eventPercent: Int,
    @SerialName("platform_percent")
    val platformPercent: Int,
    @SerialName("created_at")
    val createdAt: String
)