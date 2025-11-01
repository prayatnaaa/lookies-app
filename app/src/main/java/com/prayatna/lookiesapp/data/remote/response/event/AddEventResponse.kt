package com.prayatna.lookiesapp.data.remote.response.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEventResponse(
    val message: String,
    @SerialName("event_id") val eventId: String? = null
)