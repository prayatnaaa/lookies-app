package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.DefaultEventDto
import com.prayatna.lookiesapp.domain.model.event.DefaultEvent

fun DefaultEventDto.toDomain(): DefaultEvent {
    return DefaultEvent(
        id = id,
        title = title,
        organizerId = organizerId,
        bannerImageUrl = bannerImageUrl,
        startDate = startDate,
        endDate = endDate,
        about = about,
        location = location,
        locationUrl = locationUrl,
        maxParticipant = maxParticipant,
        maxPainting = maxPainting,
        maxPaintingPerArtist = maxPaintingPerArtist,
        ticketPrice = ticketPrice,
        artistRegistrationFee = artistRegistrationFee,
        eventType = eventType,
        eventFormat = eventFormat,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}