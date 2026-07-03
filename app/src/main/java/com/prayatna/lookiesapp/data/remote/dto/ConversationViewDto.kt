package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationViewDto(

    @SerialName("id")
    val conversationId: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("buyer_name")
    val buyerName: String? = null,

    @SerialName("buyer_picture_url")
    val buyerPictureUrl: String? = null,

    @SerialName("merchant_id")
    val merchantId: String,

    @SerialName("merchant_name")
    val merchantName: String? = null,

    @SerialName("merchant_picture_url")
    val merchantPictureUrl: String? = null,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("last_message_content")
    val lastMessageContent: String? = null,

    @SerialName("last_message_time")
    val lastMessageTime: String? = null,

    @SerialName("last_message_sender")
    val lastMessageSender: String? = null,

    @SerialName("last_message_is_read")
    val lastMessageIsRead: Boolean? = null
)