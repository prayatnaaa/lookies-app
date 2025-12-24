package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import javax.inject.Inject

class GetSelfEventsUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend operator fun invoke() = partnerRepository.getSelfEvents()
}