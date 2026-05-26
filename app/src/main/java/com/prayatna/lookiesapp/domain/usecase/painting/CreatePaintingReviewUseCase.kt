package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.model.painting.CreatePaintingReviewInput
import com.prayatna.lookiesapp.domain.model.painting.PaintingReview
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreatePaintingReviewUseCase @Inject constructor(
    private val repository: PaintingRepository
) {
    suspend operator fun invoke(paintingReview: CreatePaintingReviewInput): DataResult<PaintingReview> {
        return repository.createPaintingReview(paintingReview)
    }
}