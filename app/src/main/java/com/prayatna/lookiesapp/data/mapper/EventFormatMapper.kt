package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventFormatDto
import com.prayatna.lookiesapp.domain.model.event.EventFormat

fun EventFormatDto.toDomain(): EventFormat {
    return EventFormat(
        id = this.id,
        name = this.name,
        slug = this.slug
    )
}