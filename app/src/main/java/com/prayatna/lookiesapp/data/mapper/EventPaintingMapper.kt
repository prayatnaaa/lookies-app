package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventPaintingDto
import com.prayatna.lookiesapp.domain.model.painting.EventPainting

fun EventPaintingDto.toDomain(): EventPainting {
    return EventPainting(
        id = this.id,
        finalPrice = this.finalPrice,
        status = this.status,
        createdAt = this.createdAt,
        painting = this.painting.toDomain(),
        participant = this.participant.toDomain()
    )
}