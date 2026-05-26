package com.prayatna.lookiesapp.domain.model.partner

data class PartnerDashboard(
    val partnerId: String,
    val partnerName: String?,
    val totalEventsCreated: Int,
    val activeEvents: Int,
    val totalTicketsSold: Int,
    val totalRevenue: Double,
    val pendingPayout: Double
)