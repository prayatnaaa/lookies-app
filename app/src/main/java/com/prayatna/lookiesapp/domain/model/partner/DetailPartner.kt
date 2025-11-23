package com.prayatna.lookiesapp.domain.model.partner

import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.domain.model.user.Profile

data class DetailPartner(
    val profileId: Int?,
    val name: String,
    val type: String,
    val logoUrl: String,
    val portfolioLink: String,
    val status: String,
    val locations: List<Location>,
    val profile: Profile
)
