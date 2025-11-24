package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

data class DetailPartnerUiModel(
    val id: Int?,
    val name: String? = null,
    val type: String? = null,
    val logoUrl: String? = null,
    val portfolioLink: String? = null,
    val status: String? = null,
    val locations: List<LocationUiModel>,
    val profile: ProfileUiModel? = null
)

data class LocationUiModel (
    val name: String? = null,
    val locUrl: String? = null
)

data class ProfileUiModel (
    val id: String? = "",
    val profileUrl: String? = "",
    val username: String? = "",
    val fullName: String? = "",
    val address: String? = "",
    val bio: String? = "",
    val hasPartnerSub: Boolean? = false
)