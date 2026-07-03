package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.model.artist.GetArtistBusinessIdOutput
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    suspend fun registerEvent(
        eventId: Int,
        paintingIds: List<Int>,
        commissionRate: Double
    ):
            DataResult<RegisterEventOutput>
    fun getDashboardData():
            Flow<ArtistDashboardSummary>
    suspend fun getArtistEventPaintings(artistId: String, status: String? = null):
            DataResult<List<EventPainting>>
    suspend fun getArtistBusinessId():
            DataResult<GetArtistBusinessIdOutput>
}