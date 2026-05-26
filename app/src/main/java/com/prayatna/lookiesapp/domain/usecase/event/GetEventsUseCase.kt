package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        title: String? = null,
        organizerId: String? = null,
        status: String? = null,
        location: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        isTicketPriceAscending: Boolean = true,
        limitCount: Long? = null
    ): DataResult<List<Event>> {
        val result =  repository.getEvents(
            limitCount = limitCount,
            title = title,
            organizerId = organizerId,
            status = status,
            location = location,
            startDate = startDate,
            endDate = endDate,
            isTicketPriceAscending = isTicketPriceAscending
        )
        return result
    }
}