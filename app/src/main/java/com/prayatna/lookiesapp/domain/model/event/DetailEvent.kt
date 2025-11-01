package com.prayatna.lookiesapp.domain.model.event


data class DetailEvent(
    val locationUrl: String,
    val ticketQuantity: Int,
    val startTime: String,
    val endTime: String
)