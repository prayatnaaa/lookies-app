package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.event.CreateEventRequest
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams

fun CreateEventParams.toDto(): CreateEventRequest {
    return CreateEventRequest(
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
        registrationFee = this.registrationFee,
        eventType = this.eventType,
        eventFormat = this.eventFormat,
        organizerId = this.organizerId,
        paintingArtistPercent = this.paintingArtistPercent,
        paintingEventPercent = this.paintingEventPercent,
        paintingPlatformPercent = this.paintingPlatformPercent,
        ticketArtistPercent = this.ticketArtistPercent,
        ticketEventPercent = this.ticketEventPercent,
        ticketPlatformPercent = this.ticketPlatformPercent
    )
}