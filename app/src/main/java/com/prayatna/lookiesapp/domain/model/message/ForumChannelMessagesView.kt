package com.prayatna.lookiesapp.domain.model.message


data class ForumChannelMessagesView(
    val id: String,
    val channelId: String,
    val senderId: String,
    val content: String,
    val email: String,
    val createdAt: String,
    val fullName: String,
    val profilePictureUrl: String? = null,
    val isPinned: Boolean = false,
    val editedAt: String? = null
)
