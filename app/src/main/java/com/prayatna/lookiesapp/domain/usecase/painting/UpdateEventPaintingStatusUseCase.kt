package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateEventPaintingStatusUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(eventPaintingId: String, status: String): DataResult<Unit> {
        return paintingRepository.updateEventPaintingStatus(eventPaintingId, status)
    }
}
