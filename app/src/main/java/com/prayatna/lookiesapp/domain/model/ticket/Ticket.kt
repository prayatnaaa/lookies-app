package com.prayatna.lookiesapp.domain.model.ticket

data class Ticket(
    val id: String,
    val eventId: Int,
    val userId: String,
    val orderId: String,
    val ticketCode: String,
    val createdAt: String
)
