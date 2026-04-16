package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.BuildConfig
import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.request.artist.RegisterEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.GetArtistBusinessIdResponse
import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SupabaseArtistService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val realtime: Realtime,
    private val httpClient: HttpClient,
) {

    suspend fun getArtistBusinessId(): GetArtistBusinessIdResponse {
        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")
        return postgrest.from("merchant_accounts").select(
            Columns.list("business_id")
        ) {
            filter {
                eq("owner_user_id", session.user!!.id)
            }
        }.decodeSingle<GetArtistBusinessIdResponse>()
    }

    suspend fun registerEvent(
        artistId: String,
        eventId:Int,
        paintingIds: List<Int>
    ): RegisterEventResponse {
        val parameters = RegisterEventRequest(
            artistId = artistId,
            eventId = eventId,
            paintingIds = paintingIds
        )

        val session = auth.currentSessionOrNull()
            ?: throw IllegalStateException("No active session")

        val response =  httpClient.post("${BuildConfig.SUPABASE_EDGE_BASE_URL}/register-paintings-to-event") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${session.accessToken}")
            setBody(parameters)
        }

        Log.d("Create-Event", response.body())

        return response.body()
    }

    suspend fun getArtistEventPaintings(artistId: String): List<EventPaintingDto> {
        val response = postgrest.from("event_paintings_view")
            .select {
                filter {
                    eq("event_participants->>artist->>user_id", artistId)
                }
            }.decodeList<EventPaintingDto>()
        return response
    }

    fun getDashboardSummary(): Flow<ArtistDashboardSummaryDto> = callbackFlow {

        val userId = auth.currentSessionOrNull()?.user?.id
            ?: throw IllegalStateException("User not logged in")

        val job = launch {

            val initial = postgrest
                .from("artist_dashboard")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeSingle<ArtistDashboardSummaryDto>()

            Log.d("Dashboard", "initial: $initial")

            trySend(initial)

            val channel = realtime.channel("dashboard:$userId")

            channel
                .broadcastFlow<ArtistDashboardSummaryDto>(event = "dashboard_update")
                .onEach { trySend(it) }
                .launchIn(this)

            channel.subscribe(blockUntilSubscribed = true)
        }

        awaitClose {
            job.cancel()
        }
    }
}