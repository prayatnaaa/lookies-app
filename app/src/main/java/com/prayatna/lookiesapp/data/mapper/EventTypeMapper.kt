package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.EventTypeDto
import com.prayatna.lookiesapp.domain.model.event.TEventType

fun EventTypeDto.toDomain(): TEventType {
    return TEventType(
        id = this.id,
        name = this.name,
        slug = this.slug
    )
}