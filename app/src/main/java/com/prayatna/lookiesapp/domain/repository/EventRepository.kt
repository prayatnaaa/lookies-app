package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEvents(
        title: String? = null,
        organizerId: String? = null,
        status: String? = null,
        location: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        isTicketPriceAscending: Boolean = true,
        limitCount: Long? = null,
        eventType: String? = null,
        eventFormat: String? = null
    ): DataResult<List<Event>>
    suspend fun getEvent(eventId: String, forceRefresh: Boolean = false): DataResult<Event>
    suspend fun getEventStatistics(eventId: String): Flow<DataResult<EventStatisticDto>>
    suspend fun createEvent(params: CreateEventParams, imageByte: Uri): DataResult<Event>
    suspend fun getEventTypes(): DataResult<List<TEventType>>
    suspend fun getEventFormats(): DataResult<List<EventFormat>>
    suspend fun getEventPaintings(eventId: String, status: String? = null): DataResult<List<EventPainting>>
    suspend fun getRevenueRulesByEventId(eventId: Int): DataResult<List<EventRevenueRules>>
    suspend fun deleteEvent(eventId: String): DataResult<Unit>
}