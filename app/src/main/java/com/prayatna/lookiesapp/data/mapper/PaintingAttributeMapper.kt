package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.painting.PaintingAttributeResponse
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute

fun PaintingAttributeResponse.toDomain(): PaintingAttribute {
    return PaintingAttribute(
        id = id,
        code = code,
        name = name
    )
}