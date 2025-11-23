package com.prayatna.lookiesapp.domain.model.partner

import com.prayatna.lookiesapp.domain.model.location.Location

data class Partner(
    val name: String,
    val type: String,
    val logoUrl: String,
    val status: String,
    val locations: List<Location>
)
