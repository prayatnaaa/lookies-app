package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseApi
import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.response.event.AddEventResponse
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

interface EventRepository {
    fun getEvents(): Flow<DataResult<List<EventDto>>>
    suspend fun getEvent(eventId: String): DataResult<EventDto>
    suspend fun addEvent(event: EventDto, detailEvent: DetailEventDto, imageByte: ByteArray): DataResult<AddEventResponse>
    suspend fun editEvent(event: EventDto): DataResult<String>
    suspend fun deleteEvent(eventId: String): DataResult<String>
}

class EventRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userPreference: UserPreference,
    private val supabaseApi: SupabaseApi,
    private val auth: Auth
): EventRepository {
    override fun getEvents(): Flow<DataResult<List<EventDto>>> = flow {
        try {
            val events = postgrest
                .from("events")
                .select()
                .decodeList<EventDto>()

            emit(DataResult.Success(events))
        } catch (e: RestException) {
            when (e) {
                is BadRequestRestException -> emit(DataResult.Error(e.error))
                is NotFoundRestException -> emit(DataResult.Error(e.error))
                is UnauthorizedRestException -> emit(DataResult.Error(e.error))
                is UnknownRestException -> emit(DataResult.Error(e.error))
            }
        } catch (e: HttpRequestException) {
            emit(DataResult.Error(e.message.toString()))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message.toString()))
        }
    }

    override suspend fun getEvent(eventId: String): DataResult<EventDto> {
        return try {
            val event = postgrest
                .from("events")
                .select {
                    filter {
                        eq("id", eventId)
                    }
                }
                .decodeSingle<EventDto>()

            DataResult.Success(event)
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
            val userId = userPreference.userIdPreference.first()
                ?: return DataResult.Error("User not logged in")
            val token = auth.currentAccessTokenOrNull()
                ?: return DataResult.Error("Missing auth token")

            val eventDto = event.copy(
                organizerId = userId,
                bannerImageUrl = imageUrl
            )

            val response = supabaseApi.addEvent(
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