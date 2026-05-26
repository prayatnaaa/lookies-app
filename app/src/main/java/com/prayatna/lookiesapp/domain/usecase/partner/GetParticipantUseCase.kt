package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.model.EventParticipant
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetParticipantUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend operator fun invoke(eventId: String?): DataResult<List<EventParticipant>> {
        val response = partnerRepository.getParticipantList(eventId = eventId)
       return response
    }
}