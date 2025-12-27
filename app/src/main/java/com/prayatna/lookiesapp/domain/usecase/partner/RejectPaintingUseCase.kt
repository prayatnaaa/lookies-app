package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class RejectPaintingUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(id: String): DataResult<EventPainting> {
        return repository.rejectPainting(id)
    }
}