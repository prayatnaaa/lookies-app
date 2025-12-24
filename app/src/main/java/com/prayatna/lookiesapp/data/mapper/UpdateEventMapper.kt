package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.event.UpdateEventRequest
import com.prayatna.lookiesapp.domain.model.event.EditEventInput

fun EditEventInput.toDto(): UpdateEventRequest {
    return UpdateEventRequest(
        title = this.title,
        startDate = this.startDate,
        endDate = this.endDate,
        about = this.about,
        location = this.location,
        locationUrl = this.locationUrl,
        maxParticipant = this.maxParticipant,
        maxPainting = this.maxPainting,
        maxPaintingPerArtist = this.maxPaintingPerArtist,
        ticketPrice = this.ticketPrice,
        registrationFee = this.registrationFee,
        eventType = this.eventType,
        eventFormat = this.eventFormat
    )
}