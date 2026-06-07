package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class DeleteEventPaintingUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend operator fun invoke(id: String): DataResult<String> {
        if (id.isBlank()) {
            return DataResult.Error("Painting ID cannot be empty")
        }
        return partnerRepository.deleteEventPainting(id)
    }
}
