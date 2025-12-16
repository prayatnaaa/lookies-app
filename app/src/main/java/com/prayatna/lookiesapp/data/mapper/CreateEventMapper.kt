package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.event.CreateEventRequest
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams

fun CreateEventRequest.toDomain(): CreateEventParams {
    return CreateEventParams(
        title = this.title,
        bannerImageUrl = this.bannerImageUrl,
        startDate = this.startDate,
        endDate = this.endDate,
        about = this.about,
        location = this.location,
        locationUrl = this.locationUrl,
        maxParticipant = this.maxParticipant,
        maxPainting = this.maxPainting,
        maxPaintingPerArtist = this.maxPaintingPerArtist,
        ticketPrice = this.ticketPrice,
        registrationFee = this.registrationFee
    )
}