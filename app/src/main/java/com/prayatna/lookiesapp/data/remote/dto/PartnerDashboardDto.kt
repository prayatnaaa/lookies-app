package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerDashboardDto(
    @SerialName("partner_id")
    val partnerId: String,

    @SerialName("partner_name")
    val partnerName: String?,

    @SerialName("total_events_created")
    val totalEventsCreated: Int,

    @SerialName("active_past_events")
    val activeEvents: Int,

    @SerialName("total_tickets_sold")
    val totalTicketsSold: Int,

    @SerialName("total_revenue_all_time")
    val totalRevenue: Double,

    @SerialName("pending_payout")
    val pendingPayout: Double
)