package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.dto.EventFormatDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.EventStatisticDto
import com.prayatna.lookiesapp.data.remote.dto.EventTypeDto
import com.prayatna.lookiesapp.data.remote.dto.request.event.CreateEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.event.CreateEventResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.UUID
import javax.inject.Inject

class SupabaseEventService @Inject constructor(
    private val httpClient: HttpClient,
    private val auth: Auth,
    private val storage: Storage,
    private val postgrest: Postgrest
) {

    suspend fun createEvent(
        request: CreateEventRequest,
        bannerImage: ByteArray?
    ): CreateEventResponse {

        if (bannerImage == null) {
            throw IllegalArgumentException("Banner image cannot be null")
        }

        var uploadedPath: String? = null

        try {
            val path = "events/${request.organizerId}/${UUID.randomUUID()}.png"

            storage.from("partner_assets").upload(
                path = path,
                data = bannerImage,
                upsert = true
            )

            uploadedPath = path

            val bannerUrl = Helper.buildImageUrl(
                bucketName = "partner_assets",
                imageName = path
            )

            val finalRequest = request.copy(
                bannerImageUrl = bannerUrl
            )

            val session = auth.currentSessionOrNull()
                ?: throw IllegalStateException("No active session")

            Log.d("Create-Event", "Access token: ${session.accessToken}")

            val response =  httpClient.post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/create-event") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${session.accessToken}")
                setBody(finalRequest)
            }

            Log.d("Create-Event", response.body())

            return response.body()

        } catch (e: Exception) {
            uploadedPath?.let {
                storage.from("event-banners").delete(it)
            }
            throw e
        }
    }

    suspend fun getEventStatistics(eventId: String): EventStatisticDto {
        val eventStatistics = postgrest.from("event_statistics_view")
            .select {
                filter {
                    eq("event_id", eventId)
                }
            }
            .decodeSingle<EventStatisticDto>()
        return eventStatistics
    }

    suspend fun getEventTypes(): List<EventTypeDto> {
        val eventTypes = postgrest.from("event_types")
            .select()
            .decodeList<EventTypeDto>()
        return eventTypes
    }

    suspend fun getEventFormats(): List<EventFormatDto> {
        val eventFormats = postgrest.from("event_formats")
            .select()
            .decodeList<EventFormatDto>()
        return eventFormats
    }

    suspend fun getEvents(
        title: String? = null,
        organizerId: String? = null,
        status: String? = null
    ): List<EventDto> {

        val query = postgrest
            .from("events_view")
            .select {
                filter {
                    if (title != null) {
                        ilike("title", "%$title%")
                    }
                    if (organizerId != null) {
                        eq("organizer_id", organizerId)
                    }
                    if (status != null) {
                        eq("status", status)
                    }
                }
            }

        return query.decodeList()
    }


    suspend fun getDetailEvent(id: String): EventDto {
        val event = postgrest.from("events_view").select {
            filter {
                eq("event_id", id)
            }
        }.decodeSingle<EventDto>()

        Log.d("getDetailEvent", event.toString())
        return event
    }

    suspend fun getEventPaintings(participantId: String, status: String? = null): List<EventPaintingDto> {
        val response = postgrest
            .from("event_paintings_view")
            .select {
                filter {
                    eq("event_participants->>id", participantId)
                    if (status != null) {
                        eq("status", status)
                    }
                }
            }
            .decodeList<EventPaintingDto>()

        Log.d("getEventPaintings", response.toString())
        return response
    }
}