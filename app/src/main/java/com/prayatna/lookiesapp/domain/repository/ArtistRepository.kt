package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.utils.DataResult

interface ArtistRepository {
    suspend fun registerEvent(eventId: Int,
                              paintingIds: List<Int>): DataResult<RegisterEventOutput>
}