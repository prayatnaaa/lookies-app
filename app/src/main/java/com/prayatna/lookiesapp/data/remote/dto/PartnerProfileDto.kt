package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerProfileDto(
    @SerialName("profile_id") val profileId: String,
    val name: String,
    val type: String,
    @SerialName("logo_url") val logoUrl: String,
    @SerialName("portfolio_link") val portfolioLink: String,
    val status: String,
    val locations: LocationDto,
    val profile: ProfileDto
)
