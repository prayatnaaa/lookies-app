package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PartnerDto
import com.prayatna.lookiesapp.domain.model.partner.Partner

fun PartnerDto.toDomain(): Partner {
    return Partner(
        id = this.id,
        name = this.name,
        logoUrl = this.logoUrl,
        status = this.status,
    )
}