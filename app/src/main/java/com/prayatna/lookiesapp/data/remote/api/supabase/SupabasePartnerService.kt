package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
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
        val events = postgrest.from("events")
            .select {
                filter {
                    eq("organizer_id", user.id)
                }
            }.decodeList<EventDto>()

        return events
    }

    suspend fun updateEvent(id: String, request: UpdateEventRequest): EventDto {
        val response = postgrest.from("events")
            .update(request) {
                select()
                filter {
                    eq("id", id)
                }
            }.decodeSingle<EventDto>()
        return response
    }

    suspend fun getParticipantList(eventId: String?): List<EventParticipantDto> {
        val response = postgrest
            .from("event_participants")
            .select(
                columns = Columns.raw(
                    """
                id,
                status,
                event:events (
                    id,
                    organizer_id,
                    title,
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
                artist:user_profiles (
                    user_id,
                    bio,
                    address,
                    username,
                    full_name,
                    has_partner_sub,
                    profile_picture_url,
                    is_artist
                )
                """.trimIndent()
                )
            ) {
                if (eventId != null) {
                    filter {
                        eq("event_id", eventId)
                    }
                }
            }
            .decodeList<EventParticipantDto>()
        Log.d("GetParticipant", response.toString())
        return response
    }
}