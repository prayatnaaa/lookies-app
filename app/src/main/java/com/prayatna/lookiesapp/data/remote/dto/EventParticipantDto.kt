package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventParticipantDto (
    val id: String,
    val event: EventDto,
    val artist: ProfileDto,
    val status: String,
)