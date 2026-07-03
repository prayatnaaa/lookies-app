package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.chat.CreateForumChannelResponseDto
import com.prayatna.lookiesapp.domain.model.message.CreateForumChannelResult

fun CreateForumChannelResponseDto.toDomain(): CreateForumChannelResult {
    return CreateForumChannelResult(
        id = this.id,
        forumId = this.forumId,
        name = this.name,
        isReadOnlyForMembers = this.isReadOnlyForMembers,
        createdAt = this.createdAt
    )
}
