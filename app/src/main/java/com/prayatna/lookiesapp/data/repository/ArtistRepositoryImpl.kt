package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseArtistService
import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardDataDto
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardData
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val supabaseArtistService: SupabaseArtistService,
    private val auth: Auth
): ArtistRepository {
    override suspend fun registerEvent(
        eventId: Int,
        paintingIds: List<Int>
    ): DataResult<RegisterEventOutput> {
        val artistId = auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        return try {
            val response = supabaseArtistService.registerEvent(
                artistId = artistId,
                eventId = eventId,
                paintingIds = paintingIds
            )

            if (response.status == "error") {
                return DataResult.Error(response.message)
            }

            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            Log.e("RegisterEvent", e.toString())
            DataResult.Error(e.error)
        }
    }

    override fun getDashboardData(): Flow<DataResult<ArtistDashboardData>> {
        return try {
            combine(
                supabaseArtistService.getDashboardSummary(),
                supabaseArtistService.getMonthlySalesStream()
            ) { summary, salesList ->

                val data = ArtistDashboardDataDto(
                    summary = summary,
                    monthlySales = salesList.sortedByDescending { it.monthYear }.take(6)
                )

                DataResult.Success(data.toDomain())
            }
        } catch (e: RestException) {
            flowOf(DataResult.Error(e.error))
        }
    }

    override suspend fun getArtistEventPaintings(artistId: String): DataResult<List<EventPainting>> {
        return try {
            val response = supabaseArtistService.getArtistEventPaintings(artistId = artistId)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(e.error)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }
}