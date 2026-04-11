package com.prayatna.lookiesapp.data.remote.dto.request.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateForumMessageRequest(
    @SerialName("channel_id")
    val channelId: String,
    @SerialName("sender_id")
    val senderId: String,
    val content: String
)