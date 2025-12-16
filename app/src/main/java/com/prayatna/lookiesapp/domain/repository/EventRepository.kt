package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.utils.DataResult

interface EventRepository {
    suspend fun getEvents(): DataResult<List<Event>>
    suspend fun getEvent(eventId: String, forceRefresh: Boolean = false): DataResult<Event>
    suspend fun createEvent(params: CreateEventParams, imageByte: Uri): DataResult<Event>
}