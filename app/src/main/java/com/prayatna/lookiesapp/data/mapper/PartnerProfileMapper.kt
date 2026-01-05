package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PartnerProfileDto
import com.prayatna.lookiesapp.domain.model.partner.Partner

fun PartnerProfileDto.toDomain(): Partner {
    return Partner(
        id = this.id,
        name = this.name,
        logoUrl = this.logoUrl ?: "",
        status = this.type ?: ""
    )
}