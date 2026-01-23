package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventStatisticDto(
    @SerialName("event_id") val eventId: Int,
    @SerialName("total_revenue") val totalRevenue: Double,
    @SerialName("tickets_sold") val ticketsSold: Long,
    @SerialName("ticket_revenue") val ticketRevenue: Double,
    @SerialName("paintings_sold") val paintingsSold: Long,
    @SerialName("painting_revenue") val paintingRevenue: Double,
    @SerialName("approved_artists") val approvedArtists: Long,
    @SerialName("pending_participants") val pendingParticipants: Long,
    @SerialName("total_paintings") val totalPaintings: Long
)