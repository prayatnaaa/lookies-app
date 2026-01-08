package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.DefaultEventDto
import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.EventParticipantDto
import com.prayatna.lookiesapp.data.remote.dto.PartnerDto
import com.prayatna.lookiesapp.data.remote.dto.request.event.UpdateEventRequest
import com.prayatna.lookiesapp.utils.Helper
import com.prayatna.lookiesapp.utils.JsonProvider
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import java.util.UUID
import javax.inject.Inject

class SupabasePartnerService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val httpClient: HttpClient
) {
    private suspend fun uploadPartnerLogo(image: ByteArray): String {
        if (image.isEmpty()) throw Exception("Image is empty")

        val userId = auth.currentUserOrNull()?.id
            ?: throw Exception("User not authenticated")

        val path = "partner-logos/${UUID.randomUUID()}.png"
        val bucketName = "partner_assets"

        val imageUrl = storage.from(bucketName).upload(
            path = path,
            data = image,
            upsert = true
        )

        val fullPublicUrl = Helper.buildImageUrl(imageName = imageUrl, bucketName = bucketName)

        val updateResult = postgrest.from("partner_profiles")
            .update({
                set("logo_url", fullPublicUrl)
            }) {
                filter { eq("profile_id", userId) }
            }

        Log.d("PartnerLogo", "Logo updated: ${updateResult.data}")
        return fullPublicUrl
    }

    suspend fun getPartners(): List<PartnerDto> {
        val result = postgrest
            .from("partner_profiles").select(
                columns = Columns.list("user_id", "name", "logo_url", "status")
            )
            .decodeList<PartnerDto>()
        return result
    }

    suspend fun getDetailPartner(id: String): DetailPartnerDto {
        Log.d("PartnerRepository", "Getting detail partner with id: $id")
        val response: HttpResponse = httpClient
            .get("${BuildConfig.SUPABASE_EDGE_BASE_URL}/get-detail-partner?id=${id}") {
                auth.currentSessionOrNull()?.let {
                    header("Authorization", "Bearer ${it.accessToken}")
                }
            }
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Failed! ${response.status}")
        }
        return JsonProvider.json.decodeFromString(response.body())
    }

    suspend fun getSelfEvents(): List<EventDto> {
        val user = auth.currentUserOrNull()
            ?: throw Exception("user not logged in")
        val events = postgrest.from("event_detail_view")
            .select {
                filter {
                    eq("organizer_id", user.id)
                }
            }.decodeList<EventDto>()

        return events
    }

    suspend fun updateEvent(id: String, request: UpdateEventRequest): DefaultEventDto {
        val response = postgrest.from("events")
            .update(request) {
                select()
                filter {
                    eq("id", id)
                }
            }.decodeSingle<DefaultEventDto>()
        return response
    }

    suspend fun getParticipantList(eventId: String?): List<EventParticipantDto> {
        val response = postgrest
            .from("event_participant_view")
            .select {
                if (eventId != null) {
                    filter {
                        eq("event->>event_id", eventId)
                    }
                }
            }
            .decodeList<EventParticipantDto>()
        Log.d("GetParticipant", response.toString())
        return response
    }

    suspend fun approvePainting(eventPaintingId: String): EventPaintingDto {
        val result = postgrest
            .from("event_paintings")
            .update(
                mapOf(
                    "status" to "accepted",
                    "updated_at" to "now()"
                )
            ) {
                select(
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
                )
                filter {
                    eq("id", eventPaintingId)
                    eq("status", "pending")
                }
            }.decodeSingle<EventPaintingDto>()
        Log.d("ApprovePainting", result.toString())
        return result
    }

    suspend fun rejectPainting(eventPaintingId: String): EventPaintingDto {
        val result = postgrest
            .from("event_paintings")
            .update(
                mapOf(
                    "status" to "rejected",
                    "updated_at" to "now()"
                )
            ) {
                select(
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
                )
                filter {
                    eq("id", eventPaintingId)
                    eq("status", "pending")
                }
            }.decodeSingle<EventPaintingDto>()
        return result
    }
}