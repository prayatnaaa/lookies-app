package com.prayatna.lookiesapp.domain.model.partner

import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.domain.model.user.Profile

data class DetailPartner(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val logoUrl: String? = null,
    val portfolioLink: String? = null,
    val status: String? = null,
    val locations: List<Location>,
    val profile: Profile? = null
)
