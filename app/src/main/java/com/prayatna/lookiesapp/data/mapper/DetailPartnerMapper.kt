package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner

fun DetailPartnerDto.toDomain(): DetailPartner {
    return DetailPartner(
        id = this.id,
        name = this.name,
        type = this.type,
        logoUrl = this.logoUrl,
        status = this.status,
        locations = this.locations.map { it.toDomain() },
        portfolioLink = this.portfolioLink,
        profile = this.profile?.asDomainModel(),
    )
}