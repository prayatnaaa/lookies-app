package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumChannelViewDto(
   @SerialName("channel_id")
    val id: String,
    @SerialName("forum_id")
    val forumId: String,
    val name: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    @SerialName("is_read_only_for_members")
 val isReadOnlyForMembers: Boolean
)