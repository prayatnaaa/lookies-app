package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventStatsDto(
    @SerialName("total_participants")
    val totalParticipants: Int,

    @SerialName("approved_participants")
    val approvedParticipants: Int,

    @SerialName("total_paintings")
    val totalPaintings: Int,

    @SerialName("approved_paintings")
    val approvedPaintings: Int
)
