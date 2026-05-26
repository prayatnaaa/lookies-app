package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetEventTypesUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(): DataResult<List<TEventType>> {
        return repository.getEventTypes()
    }
}
