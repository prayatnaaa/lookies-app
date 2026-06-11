package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import coil.network.HttpException
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseEventService
import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import com.prayatna.lookiesapp.utils.extractSupabaseError
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val supabaseEventService: SupabaseEventService,
    @ApplicationContext private val context: Context
): EventRepository {
    override suspend fun getEvents(
        title: String?,
        organizerId: String?,
        status: String?,
        location: String?,
        startDate: String?,
        endDate: String? ,
        isTicketPriceAscending: Boolean,
        limitCount: Long?,
        eventType: String?,
        eventFormat: String?
    ): DataResult<List<Event>> {
        return try {
            val response = supabaseEventService.getEvents(
                limitCount = limitCount,
                title = title,
                organizerId = organizerId,
                status = status,
                location = location,
                startDate = startDate,
                endDate = endDate,
                isTicketPriceAscending = isTicketPriceAscending,
                eventType = eventType,
                eventFormat = eventFormat
            )
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
        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getEventStatistics(eventId: String): Flow<DataResult<EventStatisticDto>> = flow {
        emit(DataResult.Loading)
        try {
            val idInt = eventId.toIntOrNull()
            if (idInt == null) {
                emit(DataResult.Error("Invalid Event ID"))
                return@flow
            }

            val result = supabaseEventService.getEventStatistics(eventId)
            emit(DataResult.Success(result))

        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
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
        } catch (e: RestException) {
            val eMsg = extractSupabaseError(e.error)
            DataResult.Error(eMsg)
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
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getEventFormats(): DataResult<List<EventFormat>> {
        return try {
            val response = supabaseEventService.getEventFormats()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getEventPaintings(eventId: String,
                                           status: String?): DataResult<List<EventPainting>> {
        return try {
            val response = supabaseEventService.getEventPaintings(eventId = eventId,
                status = status)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            Log.e("getEventPaintings", e.error)
            DataResult.Error(e.error)
        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRevenueRulesByEventId(eventId: Int): DataResult<List<EventRevenueRules>> {
        return try {
            val response = supabaseEventService.getRevenueRulesByEventId(eventId)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun deleteEvent(eventId: String): DataResult<Unit> {
        return try {
            supabaseEventService.deleteEvent(eventId)
            DataResult.Success(Unit)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: HttpException) {
            DataResult.Error(e.response.message)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to delete event")
        }
    }
}