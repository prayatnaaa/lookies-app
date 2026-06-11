package com.prayatna.lookiesapp.domain.model.user

data class UserNotification(
    val id: String,
    val userId: String,
    val body: String,
    val createdAt: String,
    val isRead: Boolean
)
