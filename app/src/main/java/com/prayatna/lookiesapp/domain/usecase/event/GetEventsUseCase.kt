package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(
        title: String? = null,
        organizerId: String? = null,
        status: String? = null,
        location: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        isTicketPriceAscending: Boolean = true,
        limitCount: Long? = null,
        eventType: String? = null,
        eventFormat: String? = null
    ): Flow<DataResult<List<Event>>> {
        return repository.getEvents(
            limitCount = limitCount,
            title = title,
            organizerId = organizerId,
            status = status,
            location = location,
            startDate = startDate,
            endDate = endDate,
            isTicketPriceAscending = isTicketPriceAscending,
            eventType = eventType,
            eventFormat = eventFormat
        )
    }
}
