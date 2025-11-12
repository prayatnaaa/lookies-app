package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseEventService
import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.exceptions.UnknownRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val auth: Auth,
    private val userPreference: UserPreference,
    private val supabaseEventService: SupabaseEventService,
): EventRepository {

    private val eventCache = mutableMapOf<String, DetailEventInfo>()

    override suspend fun getEvents(): DataResult<List<Event>> {
        return try {
            val eventDto = postgrest
                .from("events")
                .select()
                .decodeList<EventDto>()

            val events = eventDto.map { it.asDomainModel() }
            DataResult.Success(events)
        } catch (e: RestException) {
            when (e) {
                is BadRequestRestException -> DataResult.Error(e.error)
                is NotFoundRestException -> DataResult.Error(e.error)
                is UnauthorizedRestException -> DataResult.Error(e.error)
                is UnknownRestException -> DataResult.Error(e.error)
            }
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }


    override suspend fun getEvent(
        eventId: String,
        forceRefresh: Boolean
    ): DataResult<DetailEventInfo> {

        if (!forceRefresh && eventCache.containsKey(eventId)) {
            return DataResult.Success(eventCache[eventId]!!)
        }

        return try {
            val token = auth.currentSessionOrNull()?.accessToken
                ?: return DataResult.Error("Missing auth token")

            val response = supabaseEventService.getEvent(token = token, eventId = eventId)
            val eventInfo = response.asDomainModel()

            eventCache[eventId] = eventInfo

            DataResult.Success(eventInfo)
        } catch (e: RestException) {
            when (e) {
                is BadRequestRestException -> DataResult.Error(e.error)
                is NotFoundRestException -> DataResult.Error(e.error)
                is UnauthorizedRestException -> DataResult.Error(e.error)
                is UnknownRestException -> DataResult.Error(e.error)
            }
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error(e.message.toString())
        }
    }

    override suspend fun addEvent(
        event: EventDto,
        detailEvent: DetailEventDto,
        imageByte: ByteArray
    ): DataResult<AddEventResponse> {
        if (imageByte.isEmpty()) {
            return DataResult.Error("Image banner cannot be empty!")
        }

        return try {
            val path = "${UUID.randomUUID()}.png"

            storage.from("event_image_banner").upload(
                path = path,
                data = imageByte,
                upsert = true,
            )

            val imageUrl = Helper.buildImageUrl(imageName = path, bucketName = "event_image_banner")
            val userId = auth.currentUserOrNull()?.id
                ?: return DataResult.Error("User not logged in")
            val token = auth.currentSessionOrNull()?.accessToken
                ?: return DataResult.Error("Missing auth token")

            val eventDto = event.copy(
                organizerId = userId,
                bannerImageUrl = imageUrl
            )

            val response = supabaseEventService.addEvent(
                token = token,
                event = eventDto,
                detailEvent = detailEvent
            )

            Log.d("EVENT-ADD", "Success: ${response.message}")
            DataResult.Success(response)

        } catch (e: RestException) {
            Log.e("EVENT-GET", "Error: ${e.error}")
            when (e) {
                is BadRequestRestException -> DataResult.Error(e.error)
                is NotFoundRestException -> DataResult.Error(e.error)
                is UnauthorizedRestException -> DataResult.Error(e.error)
                else -> DataResult.Error("REST error: ${e.message}")
            }
        } catch (e: ConnectTimeoutException) {
            DataResult.Error("Connection timed out, please check your network.")
        } catch (e: Exception) {
            Log.e("EVENT-ADD", "Error: ${e.message}")
            DataResult.Error("Unexpected error: ${e.message}")
        }
    }

    override suspend fun editEvent(event: EventDto): DataResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: String): DataResult<String> {
        TODO("Not yet implemented")
    }

}