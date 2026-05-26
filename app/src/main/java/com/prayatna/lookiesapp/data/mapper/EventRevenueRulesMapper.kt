package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventRevenueRulesDto
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules

fun EventRevenueRulesDto.toDomain(): EventRevenueRules {
    return EventRevenueRules(
        id = id,
        eventId = eventId,
        itemType = itemType,
        artistPercent = artistPercent,
        eventPercent = eventPercent,
        platformPercent = platformPercent,
        createdAt = createdAt
    )
}
