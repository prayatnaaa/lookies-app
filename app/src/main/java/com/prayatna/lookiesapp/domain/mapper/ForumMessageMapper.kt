package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ForumMessageDto
import com.prayatna.lookiesapp.domain.model.message.ForumMessage

fun ForumMessageDto.toDomain() = ForumMessage (
    id = id,
    channelId = channelId,
    senderId = senderId,
    content = content,
    createdAt = createdAt
)