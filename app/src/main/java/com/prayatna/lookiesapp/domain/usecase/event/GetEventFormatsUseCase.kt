package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetEventFormatsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(): DataResult<List<EventFormat>> {
        return repository.getEventFormats()
    }
}
