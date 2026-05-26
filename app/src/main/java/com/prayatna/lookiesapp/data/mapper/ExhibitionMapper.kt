package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.domain.model.event.Exhibition
import com.prayatna.lookiesapp.data.remote.dto.ExhibitionDto

fun ExhibitionDto.asDomainModel(): Exhibition {
    return Exhibition(
        eventId = this.eventId,
        paintingId = this.paintingId,
        exhibitionPrice = this.exhibitionPrice,
        statusInEvent = this.statusInEvent,
        isAuction = this.isAuction,
        startingPrice = this.startingPrice,
        auctionEndTime = this.auctionEndTime
    )
}