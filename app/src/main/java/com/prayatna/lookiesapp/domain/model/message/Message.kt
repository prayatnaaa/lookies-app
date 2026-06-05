package com.prayatna.lookiesapp.domain.model.message

data class Message(
    val id: String?,
    val conversationId: String,
    val senderType: String,
    val senderUserId: String,
    val content: String,
    val sentAt: String?,
    val isRead: Boolean
)
