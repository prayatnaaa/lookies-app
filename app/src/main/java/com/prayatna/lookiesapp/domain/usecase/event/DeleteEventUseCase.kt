package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: String): DataResult<Unit> {
        return eventRepository.deleteEvent(eventId)
    }
}
