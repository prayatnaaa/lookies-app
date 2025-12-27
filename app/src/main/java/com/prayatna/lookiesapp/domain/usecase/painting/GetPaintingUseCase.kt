package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPaintingUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(): DataResult<List<EventPainting>> {
        return paintingRepository.getPaintings()
    }
}