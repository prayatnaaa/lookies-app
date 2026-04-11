package com.prayatna.lookiesapp.domain.model.message

data class CreateForumMessageInput(
    val channelId: String,
    val senderId: String,
    val content: String
)