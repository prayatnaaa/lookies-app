package com.prayatna.lookiesapp.data.remote.dto

import com.prayatna.lookiesapp.data.remote.dto.request.chat.MessageMetadataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(

    @SerialName("id")
    val id: String? = null,

    @SerialName("conversation_id")
    val conversationId: String,

    @SerialName("sender_type")
    val senderType: String,

    @SerialName("sender_user_id")
    val senderUserId: String,

    @SerialName("content")
    val content: String,

    @SerialName("sent_at")
    val sentAt: String? = null,

    @SerialName("is_read")
    val isRead: Boolean = false,

    @SerialName("metadata")
    val metadataDto: MessageMetadataDto? = null
)