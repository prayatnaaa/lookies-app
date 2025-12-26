package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.dto.EventFormatDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.EventTypeDto
import com.prayatna.lookiesapp.data.remote.dto.request.event.CreateEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.event.CreateEventResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
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
        val event = postgrest.from("events").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<EventDto>()

        return event
    }

    suspend fun getEventPaintings(participantId: String): List<EventPaintingDto> {
        // TODO: create FK type to medium_id and artist_id
        //painting_mediums(id, name),
        //user_profiles(user_id, full_name, username, profile_picture_url)
        val response = postgrest
            .from("event_paintings")
            .select(
                columns = Columns.raw(
                    """
                id,
                final_price,
                status,
                created_at,
                paintings(
                    id,
                    title,
                    description,
                    price,
                    painting_url,
                    year_created,
                    subject,
                    dimension_height,
                    dimension_width,
                    created_at,
                    medium_id,
                    artist_id,
                    painting_art_styles(
                        id,
                        name
                    )
                ),
                event_participants(
                    id,
                    status,
                    event:events(
                        id,
                        title,
                        organizer_id,
                        banner_image_url,
                        start_date,
                        end_date,
                        about,
                        location,
                        location_url,
                        max_participant,
                        max_painting,
                        max_painting_per_artist,
                        status,
                        ticket_price,
                        registration_fee,
                        event_type_id,
                        event_format_id,
                        created_at,
                        updated_at
                    ),
                    artist:user_profiles(
                        user_id,
                        full_name,
                        bio,
                        address,
                        username,
                        profile_picture_url,
                        has_partner_sub,
                        is_artist
                    )
                )
                """.trimIndent()
                )
            ) {
                filter {
                    eq("participant_id", participantId)
                }
            }
            .decodeList<EventPaintingDto>()

        Log.d("getEventPaintings", response.toString())
        return response
    }

}