package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    suspend fun registerEvent(
        eventId: Int,
        paintingIds: List<Int>
    ): DataResult<RegisterEventOutput>
    fun getDashboardData(): Flow<DataResult<ArtistDashboardSummary>>
    suspend fun getArtistEventPaintings(artistId: String): DataResult<List<EventPainting>>
}