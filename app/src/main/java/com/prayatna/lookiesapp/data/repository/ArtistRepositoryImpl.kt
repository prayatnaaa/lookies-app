package com.prayatna.lookiesapp.data.repository

import android.util.Log
import coil.network.HttpException
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseArtistService
import com.prayatna.lookiesapp.data.remote.dto.ArtistDashboardSummaryDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.model.artist.GetArtistBusinessIdOutput
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val supabaseArtistService: SupabaseArtistService,
    private val auth: Auth
): ArtistRepository {
    override suspend fun registerEvent(
        eventId: Int,
        paintingIds: List<Int>,
        commissionRate: Double
    ): DataResult<RegisterEventOutput> {
        val artistId = auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        return try {
            val response = supabaseArtistService.registerEvent(
                artistId = artistId,
                eventId = eventId,
                paintingIds = paintingIds,
                commissionRate = commissionRate
            )

            Log.e("RegisterEvent", "registerEvent: $response")

            if (response.status == "error") {
                return DataResult.Error(response.message)
            }

            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            Log.e("RegisterEvent", e.toString())
            DataResult.Error(e.error)
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override fun getDashboardData(): Flow<ArtistDashboardSummary> =
        supabaseArtistService
            .getDashboardSummary()
            .map { it.toDomain() }

    override suspend fun getArtistEventPaintings(artistId: String, status: String?): DataResult<List<EventPainting>> {
        return try {
            val response = supabaseArtistService.getArtistEventPaintings(artistId = artistId, status = status)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getArtistBusinessId(): DataResult<GetArtistBusinessIdOutput> {
        return try {
            val response = supabaseArtistService.getArtistBusinessId()
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        }  catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}