package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import com.prayatna.lookiesapp.domain.model.ticket.Ticket

fun TicketDto.toDomain() = Ticket(
    id = this.id,
    eventId = this.eventId,
    userId = this.userId,
    orderId = this.orderId,
    ticketCode = this.ticketCode,
    createdAt = this.createdAt
)