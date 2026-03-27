package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.request.artist.RegisterEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.channel
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
    private val realtime: Realtime
) {

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

        Log.d("RegisterEvent", parameters.toString())
        val response = postgrest.rpc(function = "v2_register_event",
            parameters = parameters).decodeAs<RegisterEventResponse>()

        return response
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