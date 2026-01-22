package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomDto(
    @SerialName("message_id")
    val messageId: Long,

    @SerialName("last_message")
    val lastMessage: String,

    @SerialName("sent_at")
    val sentAt: String,

    @SerialName("partner_id")
    val partnerId: String,

    @SerialName("partner_profile")
    val partnerProfile: ProfileDto? = null
)