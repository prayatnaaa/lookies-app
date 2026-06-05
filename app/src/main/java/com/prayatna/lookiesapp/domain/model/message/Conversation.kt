package com.prayatna.lookiesapp.domain.model.message

data class Conversation(
    val conversationId: String,
    val userId: String,
    val buyerName: String?,
    val buyerPictureUrl: String?,
    val merchantId: String,
    val merchantName: String?,
    val merchantPictureUrl: String?,
    val updatedAt: String,
    val lastMessageContent: String?,
    val lastMessageTime: String?,
    val lastMessageSender: String?,
    val lastMessageIsRead: Boolean?
)
