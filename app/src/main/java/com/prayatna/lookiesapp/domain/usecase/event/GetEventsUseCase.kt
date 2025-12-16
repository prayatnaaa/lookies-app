package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(): DataResult<List<Event>> {
        return repository.getEvents()
    }
}