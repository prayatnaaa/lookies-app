package com.prayatna.lookiesapp.data.remote.dto.request.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateForumChannelRequestDto(
    @SerialName("forum_id")
    val forumId: String,
    val name: String,
    @SerialName("is_read_only_for_members")
    val isReadOnlyForMember: Boolean = false
)