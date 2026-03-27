package com.prayatna.lookiesapp.domain.model.artist

data class RegisterEventOutput(
    val status: String,
    val message: String,
    val eventId: String? = "",
    val totalPaintings: String? = "",
    val totalAmount: Int? = null,
    val participantId: String? = "",
    val orderId: String? = ""
)