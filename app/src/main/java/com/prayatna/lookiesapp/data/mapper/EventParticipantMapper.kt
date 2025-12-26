package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventParticipantDto
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.EventParticipant

fun EventParticipantDto.toDomain(): EventParticipant {
    return EventParticipant(
        id = id,
        event = event.toDomain(),
        artist = artist.asDomainModel(),
        status = status
    )
}