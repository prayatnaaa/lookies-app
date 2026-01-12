package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.data.remote.dto.ArtistMonthlySalesDto
import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.data.remote.dto.request.artist.RegisterEventRequest
import com.prayatna.lookiesapp.data.remote.dto.response.artist.RegisterEventResponse
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
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

    @OptIn(SupabaseExperimental::class)
    fun getDashboardSummary(): Flow<ArtistDashboardSummaryDto> {
        val id = auth.currentSessionOrNull()?.user?.id ?:
        throw IllegalStateException("User not logged in")
        return postgrest.from("artist_dashboard_summary_view")
            .selectSingleValueAsFlow(ArtistDashboardSummaryDto::artistId) {
                eq("artist_id", id)
            }
    }

    @OptIn(SupabaseExperimental::class)
    fun getMonthlySalesStream(): Flow<List<ArtistMonthlySalesDto>> {
        val id = auth.currentSessionOrNull()?.user?.id ?:
        throw IllegalStateException("User not logged in")
        return postgrest.from("artist_monthly_sales_view")
            .selectAsFlow(
                ArtistMonthlySalesDto::monthYear,
                filter = FilterOperation("artist_id", FilterOperator.EQ, id)
                )
    }
}