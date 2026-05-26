package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.painting.UploadPaintingResponse
import com.prayatna.lookiesapp.domain.model.painting.UploadPaintingOutput

fun UploadPaintingResponse.toDomain(): UploadPaintingOutput {
    return UploadPaintingOutput(
        id = id,
        title = title
    )
}