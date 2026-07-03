package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumChannelMessagesViewDto(
    val id: String,
    @SerialName("channel_id")
    val channelId: String,
    @SerialName("sender_id")
    val senderId: String,
    val content: String,
    val email: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String? = null,
    @SerialName("is_pinned")
    val isPinned: Boolean = false,
    @SerialName("edited_at")
    val editedAt: String? = null
)
