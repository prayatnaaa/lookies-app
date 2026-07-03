package com.prayatna.lookiesapp.data.remote.dto.response.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateForumChannelResponseDto(
    val id: String,
    @SerialName("forum_id")
    val forumId: String,
    val name: String,
    @SerialName("is_read_only_for_members")
    val isReadOnlyForMembers: Boolean,
    @SerialName("created_at")
    val createdAt: String
)