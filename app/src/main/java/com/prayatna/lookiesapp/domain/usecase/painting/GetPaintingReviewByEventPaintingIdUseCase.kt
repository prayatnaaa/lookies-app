package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.model.painting.PaintingReview
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPaintingReviewByEventPaintingIdUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(eventPaintingId: String): DataResult<PaintingReview?> {
        return paintingRepository.getPaintingReviewByEventPaintingId(eventPaintingId)
    }
}
