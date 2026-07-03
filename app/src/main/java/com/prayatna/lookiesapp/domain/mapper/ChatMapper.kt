package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ChatMessageViewDto
import com.prayatna.lookiesapp.data.remote.dto.ConversationViewDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateMessageRequest
import com.prayatna.lookiesapp.data.remote.dto.request.chat.MessageMetadataDto
import com.prayatna.lookiesapp.domain.model.message.Conversation
import com.prayatna.lookiesapp.domain.model.message.CreateMessageInput
import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.domain.model.message.MessageMetadata
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant

fun ChatMessageViewDto.toDomain(): Message {
    return Message(
        messageId = messageId,
        conversationId = conversationId,
        senderType = senderType,
        senderUserId = senderUserId,
        content = content,
        sentAt = sentAt,
        isRead = isRead,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        metadata = metadata?.toDomain()
    )
}

fun MessageDto.toDomain(): Message {
    return Message(
        messageId = id ?: "",
        conversationId = conversationId,
        senderType = senderType,
        senderUserId = senderUserId,
        content = content,
        sentAt = sentAt?.toInstant() ?: Clock.System.now(),
        isRead = isRead,
        senderName = null,
        senderAvatarUrl = null
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = messageId,
        conversationId = conversationId,
        senderType = senderType,
        senderUserId = senderUserId ?: "",
        content = content,
        sentAt = sentAt.toString(),
        isRead = isRead
    )
}

fun ConversationViewDto.toDomain(): Conversation {
    return Conversation(
        conversationId = conversationId,
        userId = userId,
        buyerName = buyerName,
        buyerPictureUrl = buyerPictureUrl,
        merchantId = merchantId,
        merchantName = merchantName,
        merchantPictureUrl = merchantPictureUrl,
        updatedAt = updatedAt,
        lastMessageContent = lastMessageContent,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        lastMessageIsRead = lastMessageIsRead
    )
}

fun CreateMessageInput.toDto(): CreateMessageRequest {
    return CreateMessageRequest(
        conversationId = conversationId,
        senderType = senderType,
        content = content,
        metadata = metadata?.toDto()
    )
}

fun MessageMetadata.toDto(): MessageMetadataDto {
    return MessageMetadataDto(
        type = type,
        id = id,
        imageUrl = imageUrl,
        title = title
    )
}

fun MessageMetadataDto.toDomain(): MessageMetadata {
    return MessageMetadata(
        type = type,
        id = id,
        imageUrl = imageUrl,
        title = title
    )
}
