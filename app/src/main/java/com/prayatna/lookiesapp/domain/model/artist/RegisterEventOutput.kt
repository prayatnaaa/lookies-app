package com.prayatna.lookiesapp.domain.model.artist

data class RegisterEventOutput(
    val status: String,
    val message: String,
    val eventId: String? = "",
    val totalPaintings: String? = "",
    val data: SuccessRegisterEventOutput? = null
)

data class SuccessRegisterEventOutput(
    val participantId: String,
    val totalAmount: Int,
    val orderId: String
)