package com.prayatna.lookiesapp.data.remote.api.supabase

import android.util.Log
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.request.artist.RegisterEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject

class SupabaseArtistService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
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
        val response = postgrest.rpc(function = "register_event",
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
}