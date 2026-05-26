package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventStatsDto
import com.prayatna.lookiesapp.domain.model.event.EventStats

fun EventStatsDto.toDomain(): EventStats {
    return EventStats(
        totalParticipants = this.totalParticipants,
        approvedParticipants = this.approvedParticipants,
        totalPaintings = this.totalPaintings,
        approvedPaintings = this.approvedPaintings
    )
}