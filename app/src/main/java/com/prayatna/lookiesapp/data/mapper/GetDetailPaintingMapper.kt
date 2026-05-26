package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetDetailPaintingDto
import com.prayatna.lookiesapp.domain.model.painting.DetailPainting

fun GetDetailPaintingDto.toDomain(): DetailPainting {
    return DetailPainting(
        id = id,
        artistId = artistId,
        artistName = artistName,
        title = title,
        createdAt = createdAt,
        artStyleId = artStyleId,
        description = description,
        dimensionHeight = dimensionHeight,
        dimensionWidth = dimensionWidth,
        mediumId = mediumId,
        paintingUrl = paintingUrl,
        subject = subject,
        updatedAt = updatedAt,
        yearCreated = yearCreated,
        mediumName = mediumName,
        artStyleName = artStyleName,
        price = price,
        availabilityStatus = availabilityStatus
    )
}