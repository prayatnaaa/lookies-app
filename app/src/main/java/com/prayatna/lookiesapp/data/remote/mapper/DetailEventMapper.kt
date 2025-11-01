package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.domain.model.event.DetailEvent
import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto

fun DetailEvent.toDto(): DetailEventDto {
    return DetailEventDto(
        locationUrl = this.locationUrl,
        ticketQuantity = this.ticketQuantity,
        startTime = this.startTime,
        endTime = this.endTime
    )
}

fun DetailEventDto.asDomainModel(): DetailEvent {
    return DetailEvent(
        locationUrl = this.locationUrl,
        ticketQuantity = this.ticketQuantity,
        startTime = this.startTime,
        endTime = this.endTime
    )
}