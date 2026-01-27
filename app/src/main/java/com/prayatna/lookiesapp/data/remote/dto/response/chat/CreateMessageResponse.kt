package com.prayatna.lookiesapp.data.remote.dto.response.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageResponse(
    val id: Long,
    val content: String,
    @SerialName("sent_at")
    val sentAt: String
)