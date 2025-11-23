package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PartnerDto
import com.prayatna.lookiesapp.domain.model.partner.Partner

fun PartnerDto.toDomain(): Partner {
    return Partner(
        name = this.name,
        type = this.type,
        logoUrl = this.logoUrl,
        status = this.status,
        locations = this.locations.map { it.toDomain() },
    )
}