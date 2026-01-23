package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventStatisticDto(
    @SerialName("event_id") val eventId: Int,
    @SerialName("total_revenue") val totalRevenue: Double = 0.0,
    @SerialName("tickets_sold") val ticketsSold: Long = 0,
    @SerialName("ticket_revenue") val ticketRevenue: Double = 0.0,
    @SerialName("paintings_sold") val paintingsSold: Long = 0,
    @SerialName("painting_revenue") val paintingRevenue: Double = 0.0,
    @SerialName("approved_artists") val approvedArtists: Long = 0,
    @SerialName("pending_participants") val pendingParticipants: Long = 0,
    @SerialName("total_paintings") val totalPaintings: Long = 0
)