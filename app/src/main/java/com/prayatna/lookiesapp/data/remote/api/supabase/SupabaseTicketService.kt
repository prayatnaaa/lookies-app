package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.TicketDto
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class SupabaseTicketService  @Inject constructor(
    private val postgrest: Postgrest
) {
    suspend fun verifyAndConsumeTicket(ticketCode: String, eventId: Int): String {
        val ticket = postgrest["purchased_tickets"]
            .select {
                filter {
                    eq("ticket_code", ticketCode)
                    eq("event_id", eventId)
                }
            }.decodeSingleOrNull<TicketDto>()

        if (ticket == null) return "Ticket not found"
        if (ticket.isUsed) return "Ticket already used"

        postgrest["purchased_tickets"]
            .update({
                set("is_used", true)
                set("scanned_at", kotlinx.datetime.Clock.System.now())
            }) {
                select()
                filter { eq("id", ticket.id) }
            }

        return "Ticket scanned"
    }
}
