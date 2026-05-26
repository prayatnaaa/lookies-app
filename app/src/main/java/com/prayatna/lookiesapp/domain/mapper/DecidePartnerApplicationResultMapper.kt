package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.admin.DecidePartnerApplicationResponseDto
import com.prayatna.lookiesapp.domain.model.admin.DecidePartnerApplicationResult

fun DecidePartnerApplicationResponseDto.toDomain() = DecidePartnerApplicationResult(
    id = id,
    businessId = businessId,
    status = status
)