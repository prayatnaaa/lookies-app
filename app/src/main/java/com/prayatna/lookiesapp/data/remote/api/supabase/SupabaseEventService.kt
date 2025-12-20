package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.EventDto
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

        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not logged in")

        if (bannerImage == null) {
            throw IllegalArgumentException("Banner image cannot be null")
        }

        var uploadedPath: String? = null

        try {
            val path = "events/$userId/${UUID.randomUUID()}.png"

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

    suspend fun getEvents(
        title: String? = null,
        organizerId: String? = null
    ): List<EventDto> {

        val query = postgrest
            .from("approved_events")
            .select {
                filter {
                    if (title != null) {
                        eq("title", title)
                    }
                    if (organizerId != null) {
                        eq("organizer_id", organizerId)
                    }
                }
            }

        return query.decodeList()

    }


    suspend fun getDetailEvent(id: String): EventDto {
        val event = postgrest.from("approved_events").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<EventDto>()

        return event
    }
}