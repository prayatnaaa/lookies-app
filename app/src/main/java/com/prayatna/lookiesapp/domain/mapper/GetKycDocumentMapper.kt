package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.GetKycDocumentDto
import com.prayatna.lookiesapp.domain.model.admin.GetKycDocument

fun GetKycDocumentDto.toDomain() = GetKycDocument(
    id = id,
    businessId = businessId,
    type = type,
    country = country,
    fileId = fileId,
    createdAt = createdAt
)