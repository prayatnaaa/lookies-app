package com.prayatna.lookiesapp.domain.model.message

import kotlinx.datetime.Instant

data class Message(
    val messageId: String,
    val conversationId: String,
    val senderType: String,
    val senderUserId: String?,
    val content: String,
    val sentAt: Instant,
    val isRead: Boolean,
    val senderName: String?,
    val senderAvatarUrl: String?
)
