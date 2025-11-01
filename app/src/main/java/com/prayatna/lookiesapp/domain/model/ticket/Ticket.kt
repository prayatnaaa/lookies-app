package com.prayatna.lookiesapp.domain.model.ticket

data class Ticket(
    val id: Int,
    val eventId: Int,
    val userId: Int,
    val paymentId: Int,
    val ticketCode: String,
)
