package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetSelfEventsUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend operator fun invoke(
        businessId: String,
        status: String? = null,
        name: String? = null
    ): DataResult<List<Event>> {
        val result = partnerRepository.getSelfEvents(
            businessId = businessId,
            status = status,
            name = name
        )

        return result
    }
}