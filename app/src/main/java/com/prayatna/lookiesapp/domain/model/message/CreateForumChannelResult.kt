package com.prayatna.lookiesapp.domain.model.message

data class CreateForumChannelResult(
    val id: String,
    val forumId: String,
    val name: String,
    val isReadOnlyForMembers: Boolean,
    val createdAt: String
)
