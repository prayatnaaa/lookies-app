package com.prayatna.lookiesapp.data.remote.response.event

import com.prayatna.lookiesapp.data.remote.dto.DetailEventDto
import com.prayatna.lookiesapp.data.remote.dto.EventDto
import kotlinx.serialization.Serializable

@Serializable
data class DetailEventResponse(
    val event: EventDto,
    val detail: DetailEventDto,
)
