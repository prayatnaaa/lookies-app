package com.prayatna.lookiesapp.domain.usecase.event

import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

class GetEventFormatByEventPaintingIdUseCase @Inject constructor(
    private val postgrest: Postgrest
) {
    suspend operator fun invoke(eventPaintingId: String): String {
        return try {
            val paintingData = postgrest.from("event_paintings")
                .select(Columns.list("event_id")) {
                    filter { eq("id", eventPaintingId) }
                }.decodeSingle<Map<String, Int>>()
                
            val eventId = paintingData["event_id"] ?: throw Exception("Event not found")
            
            val eventData = postgrest.from("events")
                .select(Columns.list("event_format_id")) {
                    filter { eq("id", eventId) }
                }.decodeSingle<Map<String, Int>>()
                
            val formatId = eventData["event_format_id"] ?: throw Exception("Format not found")
            
            val formatData = postgrest.from("event_formats")
                .select(Columns.list("slug")) {
                    filter { eq("id", formatId) }
                }.decodeSingle<Map<String, String>>()
                
            formatData["slug"] ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}
