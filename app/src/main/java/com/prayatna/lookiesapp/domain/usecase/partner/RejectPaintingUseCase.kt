package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class RejectPaintingUseCase @Inject constructor(
    private val repository: PartnerRepository
) {
    suspend operator fun invoke(id: String, reason: String): DataResult<String> {
        return repository.rejectPainting(id, reason)
    }
}