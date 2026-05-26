package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.painting.BasePaintingDto
import com.prayatna.lookiesapp.domain.model.painting.BasePainting

fun BasePaintingDto.toDomain(): BasePainting {
    return BasePainting(
        id = id,
        artistId = artistId,
        title = title,
        createdAt = createdAt,
        description = description,
        dimensionHeight = dimensionHeight,
        dimensionWidth = dimensionWidth,
        paintingUrl = paintingUrl,
        subject = subject,
        updatedAt = updatedAt,
        yearCreated = yearCreated,
        artStyleId = artStyleId,
        mediumId = mediumId,
        price = price,
        availabilityStatus = availabilityStatus
    )
}
