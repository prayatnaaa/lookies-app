package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetRevenueRulesByEventIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: Int): DataResult<List<EventRevenueRules>> {
        return repository.getRevenueRulesByEventId(eventId)
    }
}
