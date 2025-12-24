package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import javax.inject.Inject

class EditEventUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {

    suspend operator fun invoke(
        id: String,
        input: EditEventInput
    ) = partnerRepository.updateEvent(
        id = id,
        input = input
    )
}