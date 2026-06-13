package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(eventId: String, forceRefresh: Boolean = false): Flow<DataResult<Event>> {
        return eventRepository.getEvent(eventId = eventId, forceRefresh = forceRefresh)
    }
}
