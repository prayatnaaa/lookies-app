package com.prayatna.lookiesapp.domain.model.event

data class UpdateRevenueRulesInput(
    val eventId: Int,
    val itemType: String,
    val artistPercent: Int,
    val eventPercent: Int,
    val platformPercent: Int
)
