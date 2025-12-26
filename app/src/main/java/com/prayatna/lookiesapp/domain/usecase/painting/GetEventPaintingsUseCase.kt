package com.prayatna.lookiesapp.domain.usecase.painting

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetEventPaintingsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(participantId: String): DataResult<List<EventPainting>> {
        val response = eventRepository.getEventPaintings(participantId)
        return response
    }
}