package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.data.remote.dto.EventDto


fun Event.toDto(): EventDto {
    return EventDto(
        id = this.id,
        organizerId = this.organizerId,
        title = this.title,
        bannerImageUrl = this.bannerImageUrl,
        location = this.location,
        ticketPrice = this.ticketPrice,
        registrationFee = this.registrationFee,
        date = this.date,
        status = this.status
    )
}

fun EventDto.asDomainModel(): Event {
    return Event(
        id = this.id,
        organizerId = this.organizerId,
        title = this.title,
        bannerImageUrl = this.bannerImageUrl,
        location = this.location,
        ticketPrice = this.ticketPrice,
        registrationFee = this.registrationFee,
        date = this.date,
        status = this.status
    )
}

