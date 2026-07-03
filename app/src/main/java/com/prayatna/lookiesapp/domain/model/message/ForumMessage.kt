package com.prayatna.lookiesapp.domain.model.message

data class ForumMessage(
    val id: String,
    val channelId: String,
    val senderId: String,
    val content: String,
    val createdAt: String,
    val isPinned: Boolean = false,
    val editedAt: String?
)