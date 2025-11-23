package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.DetailPartnerDto
import com.prayatna.lookiesapp.domain.model.partner.DetailPartner

fun DetailPartnerDto.toDomain(): DetailPartner {
    return DetailPartner(
        name = this.name,
        type = this.type,
        logoUrl = this.logoUrl,
        status = this.status,
        locations = this.locations.map { it.toDomain() },
        profileId = this.profileId,
        portfolioLink = this.portfolioLink,
        profile = this.profile.asDomainModel(),
    )
}