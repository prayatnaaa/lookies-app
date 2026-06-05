package com.prayatna.lookiesapp.data.remote.dto.request.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequest(
    @SerialName("conversation_id")
    val conversationId: String,
    @SerialName("sender_type")
    val senderType: String,
    @SerialName("sender_user_id")
    val senderUserId: String = "",
    @SerialName("content")
    val content: String
)
