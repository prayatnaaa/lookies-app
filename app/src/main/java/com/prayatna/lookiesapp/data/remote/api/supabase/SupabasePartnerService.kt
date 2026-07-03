package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.DefaultEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.data.remote.dto.EventParticipantDto
import com.prayatna.lookiesapp.data.remote.dto.EventRevenueRulesDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantBusinessDto
import com.prayatna.lookiesapp.data.remote.dto.MerchantDetailDto
import com.prayatna.lookiesapp.data.remote.dto.PartnerDashboardDto
import com.prayatna.lookiesapp.data.remote.dto.request.event.UpdateEventRequest
import com.prayatna.lookiesapp.data.remote.dto.request.event.UpdateRevenueRulesRequest
import com.prayatna.lookiesapp.data.remote.dto.request.painting.SelfEventPaintingInsertRequest
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.response.painting.InsertSelfEventPaintingsResponse
import com.prayatna.lookiesapp.utils.Helper
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.UUID
import javax.inject.Inject

class SupabasePartnerService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val realtime: Realtime
) {

    suspend fun deleteEventPainting(eventPaintingId: String): String {
        postgrest.from("event_paintings").delete {
            filter {
                eq("id", eventPaintingId)
            }
        }

        return "Event painting deleted successfully"
    }
    suspend fun updateRevenueRules(id: String, request: UpdateRevenueRulesRequest): EventRevenueRulesDto {
        return postgrest["event_revenue_rules"].update(request) {
            select()
            filter {
                eq("id", id)
            }
        }.decodeSingle<EventRevenueRulesDto>()
    }
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

    suspend fun getPartners(
        status: String? = null,
        name: String? = null,
        kycStatus: String? = null,
        merchantType: String? = null
    ): List<MerchantBusinessDto> {
        val result = postgrest
            .from("merchant_businesses_views")
            .select {
                filter {
                    if (status != null) {
                        eq("kyc_status", status)
                    }

                    if (name != null) {
                        ilike("legal_name", "%$name%")
                    }

                    if (kycStatus != null) {
                        eq("kyc_status", kycStatus)
                    }

                    if (merchantType != null)
                    eq("merchant_type", merchantType)
                }
            }
            .decodeList<MerchantBusinessDto>()
        Log.d("GetPartners", result.toString())

        return result
    }

    suspend fun getDetailPartner(id: String): MerchantDetailDto {
        val result = postgrest
            .from("merchant_detail_view")
            .select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<MerchantDetailDto>()
        Log.d("GetPartners", result.toString())
        return result
    }

    suspend fun getSelfEvents(
        businessId: String,
        status: String? = null,
        name: String? = null,
        eventFormatId: Int? = null,
        eventTypeId: Int? = null
    ): List<EventDto> {
        val events = postgrest.from("events_view")
            .select {
                filter {
                    eq("organizer->>id", businessId)
                    if (status != null) {
                        eq("status", status)
                    }
                    if (name != null) {
                        ilike("title", "%$name%")
                    }
                    if (eventFormatId != null) {
                        eq("event_format->>id", eventFormatId)
                    }
                    if (eventTypeId != null) {
                        eq("event_type->>id", eventTypeId)
                    }
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
                        eq("event->>id", eventId)
                    }
                }
            }
            .decodeList<EventParticipantDto>()
        Log.d("GetParticipant", response.toString())
        return response
    }

    suspend fun approvePainting(eventPaintingId: String): String {
        val paintingData = postgrest.from("event_paintings")
            .select(Columns.list("event_id")) {
                filter { eq("id", eventPaintingId) }
            }.decodeSingle<Map<String, Int>>()
            
        val eventId = paintingData["event_id"] ?: throw Exception("Event not found")

        val eventData = postgrest.from("events")
            .select(Columns.list("event_format_id")) {
                filter { eq("id", eventId) }
            }.decodeSingle<Map<String, Int>>()
            
        val formatId = eventData["event_format_id"] ?: throw Exception("Format not found")

        val formatData = postgrest.from("event_formats")
            .select(Columns.list("slug")) {
                filter { eq("id", formatId) }
            }.decodeSingle<Map<String, String>>()
            
        val isOnline = formatData["slug"] == "online"
        val newStatus = if (isOnline) "on_sale" else "accepted"

        postgrest
            .from("event_paintings")
            .update(
                mapOf(
                    "status" to newStatus,
                    "updated_at" to "now()"
                )
            ) {
                filter {
                    eq("id", eventPaintingId)
                    or {
                        eq("status", "pending")
                        eq("status", "rejected")
                    }
                }
            }
        return if (isOnline) "Painting approved and put on sale" else "Painting approved"
    }

    suspend fun rejectPainting(eventPaintingId: String, reason: String): String {
        postgrest
            .from("event_paintings")
            .update(
                mapOf(
                    "rejection_reason" to reason,
                    "status" to "rejected",
                    "updated_at" to "now()"
                )
            ) {
                filter {
                    eq("id", eventPaintingId)
                    or {
                        eq("status", "pending")
                        eq("status", "accepted")
                    }
                }
            }
        return "Painting rejected"
    }

    fun getDashboardSummary(merchantId: String): Flow<PartnerDashboardDto> = callbackFlow {
//        val userId = auth.currentUserOrNull()?.id
//            ?: throw Exception("user not logged in")

        val channel = realtime.channel("partner_dashboard_$merchantId")

        val flow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "purchased_tickets"
        }

        channel.subscribe()

        // Initial fetch
        trySend(fetchDashboardSummary(merchantId))

        val job = launch {
            flow.collect {
                trySend(fetchDashboardSummary(merchantId))
            }
        }

        awaitClose {
            job.cancel()
            launch {
                channel.unsubscribe()
                realtime.removeChannel(channel)
            }
        }
    }

    private suspend fun fetchDashboardSummary(userId: String): PartnerDashboardDto {
        return postgrest.from("partner_dashboard_view")
            .select {
                filter {
                    eq("partner_id", userId)
                }
            }
            .decodeSingle<PartnerDashboardDto>()
    }

    suspend fun insertSelfEventPaintings(
        eventId: String,
        selectedPaintings: List<GetPaintingDto>
    ): List<InsertSelfEventPaintingsResponse> {

        val insertPayload = selectedPaintings.map { painting ->
            SelfEventPaintingInsertRequest(
                eventId = eventId,
                paintingId = painting.id,
                finalPrice = painting.price,
                businessId = painting.artistId,
                status = "on_sale"
            )
        }


        @Serializable
        class RpcWrapper(val payload: List<SelfEventPaintingInsertRequest>)

        return postgrest.rpc(
            function = "insert_self_event_paintings",
            parameters = RpcWrapper(insertPayload)
        ).decodeList<InsertSelfEventPaintingsResponse>()
    }
}