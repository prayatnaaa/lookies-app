package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import com.prayatna.lookiesapp.domain.model.event.Event

fun EventDto.toDomain() : Event {
    return Event(
        id = eventId,
        title = title,
        bannerImageUrl = bannerImageUrl,
        startDate = startDate,
        endDate = endDate,
        about = about,
        location = location,
        locationUrl = locationUrl,
        maxParticipant = maxParticipant,
        maxPainting = maxPainting,
        maxPaintingPerArtist = maxPaintingPerArtist,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        ticketPrice = ticketPrice,
        artistRegistrationFee = registrationFee,
        eventType = eventType.toDomain(),
        organizer = organizer.toDomain(),
        eventFormat = eventFormat.toDomain(),
        remainingParticipantQuota = remainingParticipantQuota,
        remainingPaintingQuota = remainingPaintingQuota,
        rejectionReason = rejectionReason,
        approvedAt = approvedAt,
        approvedBy = approvedBy
    )
}