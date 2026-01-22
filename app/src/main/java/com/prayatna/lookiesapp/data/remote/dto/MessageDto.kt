package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Long,
    val content: String,
    @SerialName("sent_at")
    val sentAt: String,
    @SerialName("is_read")
    val isRead: Boolean,

    // sender
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("sender_username")
    val senderUsername: String,
    @SerialName("sender_full_name")
    val senderFullName: String,
    @SerialName("sender_avatar")
    val senderAvatar: String? = null,
    @SerialName("sender_is_artist")
    val senderIsArtist: Boolean,
    @SerialName("sender_has_partner_sub")
    val senderHasPartnerSub: Boolean,

    // receiver
    @SerialName("receiver_id")
    val receiverId: String,
    @SerialName("receiver_username")
    val receiverUsername: String,
    @SerialName("receiver_full_name")
    val receiverFullName: String,
    @SerialName("receiver_avatar")
    val receiverAvatar: String? = null,
    @SerialName("receiver_is_artist")
    val receiverIsArtist: Boolean,
    @SerialName("receiver_has_partner_sub")
    val receiverHasPartnerSub: Boolean,

    @SerialName("conversation_id")
    val conversationId: String? = null
)