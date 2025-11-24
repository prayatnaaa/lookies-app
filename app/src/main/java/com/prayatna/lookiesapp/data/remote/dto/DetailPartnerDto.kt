package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailPartnerDto(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val status: String? = null,

    @SerialName("logo_url")
    val logoUrl: String? = null,

    @SerialName("portofolio_link")
    val portfolioLink: String? = null,

    @SerialName("locations")
    val locations: List<LocationDto> = emptyList(),

    @SerialName("profile")
    val profile: ProfileDto? = null
)