package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForumMemberDto(
    @SerialName("forum_id")
    val forumId: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String? = null
)
