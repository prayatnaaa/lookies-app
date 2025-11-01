package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.utils.DataResult

interface EventRepository {
    suspend fun getEvents(): DataResult<List<Event>>
    suspend fun getEvent(eventId: String): DataResult<DetailEventInfo>
    suspend fun addEvent(event: EventDto, detailEvent: DetailEventDto, imageByte: ByteArray): DataResult<AddEventResponse>
    suspend fun editEvent(event: EventDto): DataResult<String>
    suspend fun deleteEvent(eventId: String): DataResult<String>
}