package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.LocationDto
import com.prayatna.lookiesapp.domain.model.location.Location

fun LocationDto.toDomain(): Location {
    return Location(
        userId = this.userId,
        name = this.name,
        url = this.url
    )
}