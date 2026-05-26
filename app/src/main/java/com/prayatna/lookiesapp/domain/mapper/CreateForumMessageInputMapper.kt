package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.chat.CreateForumMessageRequest
import com.prayatna.lookiesapp.domain.model.message.CreateForumMessageInput

fun CreateForumMessageInput.toDto() = CreateForumMessageRequest(
    channelId = channelId,
    senderId = senderId,
    content = content
)