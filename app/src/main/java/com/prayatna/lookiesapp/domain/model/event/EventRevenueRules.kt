package com.prayatna.lookiesapp.domain.model.event

data class EventRevenueRules(
    val id: String,
    val eventId: Int,
    val itemType: String,
    val artistPercent: Int,
    val eventPercent: Int,
    val platformPercent: Int,
    val createdAt: String
)
