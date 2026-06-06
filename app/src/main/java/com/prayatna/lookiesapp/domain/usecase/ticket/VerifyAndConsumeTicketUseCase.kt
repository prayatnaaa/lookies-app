package com.prayatna.lookiesapp.domain.usecase.ticket

import com.prayatna.lookiesapp.domain.repository.TicketRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class VerifyAndConsumeTicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(ticketCode: String, eventId: Int): DataResult<String> {
        if (ticketCode.isBlank()) return DataResult.Error("Ticket code cannot be empty")
        if (eventId <= 0) return DataResult.Error("Invalid event ID")

        return ticketRepository.verifyAndConsumeTicket(ticketCode, eventId)
    }
}
