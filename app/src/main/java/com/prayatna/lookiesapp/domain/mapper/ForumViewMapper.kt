package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ForumsViewDto
import com.prayatna.lookiesapp.domain.model.message.ForumsView

fun ForumsViewDto.toDomain() = ForumsView(
    id = id,
    eventId = eventId,
    title = title,
    bannerImageUrl = bannerImageUrl,
    createdAt = createdAt,
    startDate = startDate,
    endDate = endDate,
    role = role,
    userId = userId
)