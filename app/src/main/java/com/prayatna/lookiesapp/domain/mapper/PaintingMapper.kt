package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.painting.UploadPaintingRequest
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams

fun AddPaintingParams.toDto(): UploadPaintingRequest {
    return UploadPaintingRequest(
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