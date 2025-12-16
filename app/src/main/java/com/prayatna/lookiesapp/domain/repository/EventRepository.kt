package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.utils.DataResult

interface EventRepository {
    suspend fun getEvents(): DataResult<List<Event>>
    suspend fun getEvent(eventId: String, forceRefresh: Boolean = false): DataResult<DetailEventInfo>
    suspend fun createEvent(params: CreateEventParams, imageByte: Uri): DataResult<Event>
    suspend fun editEvent(event: EventDto): DataResult<String>
    suspend fun deleteEvent(eventId: String): DataResult<String>
}