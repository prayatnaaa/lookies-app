package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.UserNotificationDto
import com.prayatna.lookiesapp.domain.model.user.UserNotification

fun UserNotificationDto.toDomain() = UserNotification(
    id = id,
    userId = userId,
    body = body,
    createdAt = createdAt,
    isRead = isRead
)
