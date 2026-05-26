package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ForumChannelMessagesViewDto
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView

fun ForumChannelMessagesViewDto.toDomain() =
    ForumChannelMessagesView(
        id = id,
        channelId = channelId,
        senderId = senderId,
        content = content,
        email = email,
        createdAt = createdAt,
        fullName = fullName,
        profilePictureUrl = profilePictureUrl
    )