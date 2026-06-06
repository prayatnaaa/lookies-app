package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.utils.DataResult

interface TicketRepository {
    suspend fun verifyAndConsumeTicket(ticketCode: String, eventId: Int): DataResult<String>
}
