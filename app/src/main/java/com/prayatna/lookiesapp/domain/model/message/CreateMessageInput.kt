package com.prayatna.lookiesapp.domain.model.message

data class CreateMessageInput(
    val conversationId: String,
    val senderType: String,
    val content: String
)
