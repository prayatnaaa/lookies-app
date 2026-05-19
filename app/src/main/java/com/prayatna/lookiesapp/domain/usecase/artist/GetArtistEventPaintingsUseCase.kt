package com.prayatna.lookiesapp.domain.usecase.artist

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject


class GetArtistEventPaintingsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
){
    suspend operator fun invoke(artistId: String, status: String? = null): DataResult<List<EventPainting>> {
        val result = artistRepository.getArtistEventPaintings(artistId = artistId, status = status)
        return result
    }
}