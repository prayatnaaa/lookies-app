package com.prayatna.lookiesapp.data.remote.dto.response.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DecideEventResponseDto(
    val id: Int,
    @SerialName("organizer_id")
    val organizerId: String,
    val title: String,
    val status: String
)