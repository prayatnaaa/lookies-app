package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import javax.inject.Inject

class GetEventPaintingByIdUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(paintingId: String) =
        paintingRepository.getEventPaintingDetail(paintingId)
}