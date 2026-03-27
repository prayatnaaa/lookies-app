package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecideEventResponseDto
import com.prayatna.lookiesapp.domain.model.admin.DecideEventResult

fun DecideEventResponseDto.toDomain() = DecideEventResult(
    id = id,
    organizerId = organizerId,
    title = title,
    status = status
)