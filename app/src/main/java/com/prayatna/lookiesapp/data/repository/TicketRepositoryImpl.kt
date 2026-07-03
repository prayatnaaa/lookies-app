package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseTicketService
import com.prayatna.lookiesapp.domain.repository.TicketRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class TicketRepositoryImpl @Inject constructor(
    private val ticketService: SupabaseTicketService
) : TicketRepository {
    override suspend fun verifyAndConsumeTicket(
        ticketCode: String,
        eventId: Int
    ): DataResult<String> {
        return try {
            val result = ticketService.verifyAndConsumeTicket(ticketCode, eventId)
            if (result == "Ticket scanned") {
                DataResult.Success(result)
            } else {
                DataResult.Error(result)
            }
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unknown error occurred")
        }
    }
}
