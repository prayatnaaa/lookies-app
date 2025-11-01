package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.artist.ArtistApplication
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.utils.DataResult

interface ArtistRepository {
    suspend fun artistApplication(artistApplication: ArtistApplication): DataResult<String>
    suspend fun publishPainting(painting: Painting, imageFile: ByteArray): DataResult<String>
}