package com.prayatna.lookiesapp.domain.usecase.painting

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.BasePainting
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class EditPaintingUseCase @Inject constructor(
    private val paintingRepository: PaintingRepository
) {
    suspend operator fun invoke(
        painting: AddPaintingParams,
        paintingId: Int,
        image: Uri?
    ): DataResult<BasePainting> {
        return paintingRepository.editPainting(painting, paintingId, image)
    }
}
