package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseEventService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.RestException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val supabaseEventService: SupabaseEventService,
    @ApplicationContext private val context: Context
): EventRepository {
    override suspend fun getEvents(): DataResult<List<Event>> {
        return try {
            val response = supabaseEventService.getEvents()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun getEvent(
        eventId: String,
        forceRefresh: Boolean
    ): DataResult<Event> {
        return try {
            val response = supabaseEventService.getDetailEvent(eventId)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun createEvent(
        params: CreateEventParams,
        imageByte: Uri
    ): DataResult<Event> {
        val compressedBannerImage = imageByte.compressImage(context, 500_000L)
        return try {
            val result = supabaseEventService.createEvent(
                request = params.toDto(),
                bannerImage = compressedBannerImage
            )

            if (result.event != null && result.status == "success") {
                DataResult.Success(result.event.toDomain())
            } else {
                DataResult.Error(result.message)
            }
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun getEventTypes(): DataResult<List<TEventType>> {
        return try {
            val response = supabaseEventService.getEventTypes()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun getEventFormats(): DataResult<List<EventFormat>> {
        return try {
            val response = supabaseEventService.getEventFormats()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        }
    }

    override suspend fun getEventPaintings(participantId: String,
                                           status: String?): DataResult<List<EventPainting>> {
        return try {
            val response = supabaseEventService.getEventPaintings(participantId = participantId,
                status = status)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            Log.e("getEventPaintings", e.error)
            DataResult.Error(e.error)
        }
    }
}