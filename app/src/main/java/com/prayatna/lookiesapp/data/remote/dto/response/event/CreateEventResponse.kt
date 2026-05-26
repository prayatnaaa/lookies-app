package com.prayatna.lookiesapp.data.remote.dto.response.event

import com.prayatna.lookiesapp.data.remote.dto.EventDto
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventResponse(
    val message: String,
    val event: EventDto? = null,
    val status: String
)