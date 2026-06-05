package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ConversationDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.model.message.Message

fun MessageDto.toDomain(): Message {
    return Message(
        id = id,
        conversationId = conversationId,
        senderType = senderType,
        senderUserId = senderUserId,
        content = content,
        sentAt = sentAt,
        isRead = isRead
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = id,
        conversationId = conversationId,
        senderType = senderType,
        senderUserId = senderUserId,
        content = content,
        sentAt = sentAt,
        isRead = isRead
    )
}

fun ConversationDto.toDomain(): Conversation {
    return Conversation(
        id = id,
        userId = userId,
        merchantId = merchantId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
