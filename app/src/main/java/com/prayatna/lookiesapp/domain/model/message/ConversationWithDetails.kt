package com.prayatna.lookiesapp.domain.model.message

data class ConversationWithDetails(
    val conversation: Conversation,
    val otherPartyName: String,
    val otherPartyImageUrl: String?,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null
)
