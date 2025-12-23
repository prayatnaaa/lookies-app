package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.painting.UploadPaintingRequest
import com.prayatna.lookiesapp.data.remote.dto.response.painting.GetPaintingDto
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.Painting

fun GetPaintingDto.toDomain(): Painting {
    return Painting(
        id = this.id,
        artistId = this.artistId,
        title = this.title,
        paintingUrl = this.paintingUrl,
        description = this.description,
        dimensionHeight = this.dimensionHeight,
        dimensionWidth = this.dimensionWidth,
        medium = this.medium,
        artStyle = this.artStyle,
        subject = this.subject,
        yearCreated =this.yearCreated,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        price = this.price
    )
}

fun UploadPaintingRequest.toDomain(): AddPaintingParams {
    return AddPaintingParams(
        artistId = this.artistId,
        title = this.title,
        paintingUrl = this.paintingUrl,
        description = this.description,
        dimensionHeight = this.dimensionHeight,
        dimensionWidth = this.dimensionWidth,
        medium = this.medium,
        artStyle = this.artStyle,
        subject = this.subject,
        yearCreated = this.yearCreated,
        price = this.price
    )
}