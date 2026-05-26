package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import javax.inject.Inject

class InsertSelfPaintingsUseCase @Inject constructor(
    private val repository: PartnerRepository
){
    suspend operator fun invoke(eventId: Int, selectedPaintings: List<Painting>) =
        repository.insertSelfEventPaintings(eventId, selectedPaintings)
}