package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.ArtistApplication
import com.prayatna.lookiesapp.domain.model.Painting
import com.prayatna.lookiesapp.utils.DataResult

interface ArtistRepository {
    suspend fun artistApplication(artistApplication: ArtistApplication): DataResult<String>
    suspend fun publishPainting(painting: Painting, imageFile: ByteArray): DataResult<String>
}