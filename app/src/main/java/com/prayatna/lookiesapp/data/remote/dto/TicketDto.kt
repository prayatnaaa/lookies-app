package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketDto(
    val id: String,
    @SerialName("event_id")
    val eventId: Int,
    @SerialName("user_id")
    val userId: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("ticket_code")
    val ticketCode: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("is_used")
    val isUsed: Boolean,
    @SerialName("scanned_at")
    val scannedAt: String? = null
)