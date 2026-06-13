package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import coil.network.HttpException
import com.prayatna.lookiesapp.data.local.room.dao.EventDao
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.mapper.toEntity
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseEventService
import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.Event
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val supabaseEventService: SupabaseEventService,
    private val eventDao: EventDao,
    @param:ApplicationContext private val context: Context
): EventRepository {

    override fun getEvents(
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
    ): Flow<DataResult<List<Event>>> = flow {
        emit(DataResult.Loading)
        
        coroutineScope {
            // Launch background sync
            launch {
                try {
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
                    eventDao.syncEvents(response.map { it.toEntity() })
                } catch (e: Exception) {
                    Log.e("EventRepository", "Remote sync failed: ${e.message}")
                }
            }

            val statuses = status
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }

            val flow =
                if (statuses.isNullOrEmpty()) {
                    eventDao.getFilteredEventsFlow(
                        title = title,
                        eventFormat = eventFormat,
                        eventType = eventType,
                        isAsc =  if (isTicketPriceAscending) 1 else 0
                    )
                } else {
                    eventDao.getFilteredEventsWithStatusFlow(
                        title = title,
                        statuses = statuses,
                        eventType = eventType,
                        eventFormat = eventFormat,
                        isAsc = if (isTicketPriceAscending) 1 else 0
                    )
                }
            // Stream Room data to the collector
            flow.map { entities ->
                Log.d("EventsDao", entities.toString())
                DataResult.Success(entities.map { it.toDomain() }) 
            }.collect { 
                emit(it) 
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getEvent(
        eventId: String,
        forceRefresh: Boolean
    ): Flow<DataResult<Event>> = flow {
        emit(DataResult.Loading)
        
        coroutineScope {
            // Background sync
            launch {
                try {
                    val response = supabaseEventService.getDetailEvent(eventId)
                    eventDao.insertEvents(listOf(response.toEntity()))
                } catch (e: Exception) {
                    Log.e("EventRepository", "Detail sync failed for $eventId: ${e.message}")
                }
            }

            // Observe DB
            eventDao.getEventByIdFlow(eventId).map { entity ->
                if (entity != null) DataResult.Success(entity.toDomain())
                else DataResult.Loading
            }.collect { 
                emit(it) 
            }
        }
    }.flowOn(Dispatchers.IO)

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
            emit(DataResult.Error(errorMsg))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message ?: "An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createEvent(
        params: CreateEventParams,
        imageByte: Uri
    ): DataResult<Event> = withContext(Dispatchers.IO) {
        val compressedBannerImage = imageByte.compressImage(context, 500_000L)
        try {
            val result = supabaseEventService.createEvent(
                request = params.toDto(),
                bannerImage = compressedBannerImage
            )

            if (result.event != null && result.status == "success") {
                eventDao.insertEvents(listOf(result.event.toEntity()))
                DataResult.Success(result.event.toDomain())
            } else {
                DataResult.Error(result.message)
            }
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong during creation")
        }
    }

    override suspend fun getEventTypes(): DataResult<List<TEventType>> = withContext(Dispatchers.IO) {
        try {
            val response = supabaseEventService.getEventTypes()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to fetch event types")
        }
    }

    override suspend fun getEventFormats(): DataResult<List<EventFormat>> = withContext(Dispatchers.IO) {
        try {
            val response = supabaseEventService.getEventFormats()
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to fetch event formats")
        }
    }

    override suspend fun getEventPaintings(
        eventId: String,
        status: String?
    ): DataResult<List<EventPainting>> = withContext(Dispatchers.IO) {
        try {
            val response = supabaseEventService.getEventPaintings(eventId = eventId, status = status)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to fetch event artworks")
        }
    }

    override suspend fun getRevenueRulesByEventId(eventId: Int): DataResult<List<EventRevenueRules>> = withContext(Dispatchers.IO) {
        try {
            val response = supabaseEventService.getRevenueRulesByEventId(eventId)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to fetch revenue rules")
        }
    }

    override suspend fun deleteEvent(eventId: String): DataResult<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseEventService.deleteEvent(eventId)
            DataResult.Success(Unit)
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Failed to delete event")
        }
    }
}
