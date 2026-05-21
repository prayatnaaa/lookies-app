package com.prayatna.lookiesapp.domain.model.message

data class ForumMember(
    val forumId: String,
    val userId: String,
    val role: String,
    val fullName: String?,
    val profilePictureUrl: String?,
    val isOnline: Boolean = false
)
