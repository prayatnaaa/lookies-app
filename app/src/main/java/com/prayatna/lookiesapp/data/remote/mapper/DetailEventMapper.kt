package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.model.DetailEvent
import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto

fun DetailEventDto.asDomainModel(): DetailEvent {
    return DetailEvent(
        locationUrl = this.locationUrl,
        ticketQuantity = this.ticketQuantity,
        startTime = this.startTime,
        endTime = this.endTime
    )
}