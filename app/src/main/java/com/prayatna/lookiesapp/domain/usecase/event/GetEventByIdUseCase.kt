package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.repository.EventRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(eventId: String) =
        eventRepository.getEvent(eventId)
}
