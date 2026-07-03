package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ForumMemberDto
import com.prayatna.lookiesapp.domain.model.message.ForumMember

fun ForumMemberDto.toDomain(isOnline: Boolean = false, onlineAt: Long? = null): ForumMember {
    return ForumMember(
        forumId = forumId,
        userId = userId,
        role = role,
        fullName = fullName,
        profilePictureUrl = profilePictureUrl,
        isOnline = isOnline,
        onlineAt = onlineAt
    )
}
