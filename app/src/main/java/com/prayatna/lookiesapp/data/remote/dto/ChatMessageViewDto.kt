package com.prayatna.lookiesapp.data.remote.dto

import com.prayatna.lookiesapp.data.remote.dto.request.chat.MessageMetadataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class ChatMessageViewDto(

    @SerialName("message_id")
    val messageId: String,

    @SerialName("conversation_id")
    val conversationId: String,

    @SerialName("sender_type")
    val senderType: String,

    @SerialName("sender_user_id")
    val senderUserId: String? = null,

    @SerialName("content")
    val content: String,

    @SerialName("sent_at")
    val sentAt: Instant,

    @SerialName("is_read")
    val isRead: Boolean,

    @SerialName("sender_name")
    val senderName: String? = null,

    @SerialName("sender_avatar_url")
    val senderAvatarUrl: String? = null,
    val metadata: MessageMetadataDto? = null
)