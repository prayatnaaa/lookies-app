package com.prayatna.lookiesapp.domain.usecase.painting

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import javax.inject.Inject

class UploadPaintingUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(painting: AddPaintingParams, image: Uri) =
        paintingRepository.uploadPainting(painting, image)
}