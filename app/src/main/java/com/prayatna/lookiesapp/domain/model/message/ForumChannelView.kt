package com.prayatna.lookiesapp.domain.model.message

data class ForumChannelView(
    val id: String,
    val forumId: String,
    val name: String,
    val userId: String,
    val role: String
)