package com.prayatna.lookiesapp.data.remote.mapper

import com.prayatna.lookiesapp.data.model.Exhibition
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