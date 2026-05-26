package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ForumChannelViewDto
import com.prayatna.lookiesapp.domain.model.message.ForumChannelView

fun ForumChannelViewDto.toDomain() = ForumChannelView(
    id = id,
    forumId = forumId,
    name = name,
    userId = userId,
    role = role
)