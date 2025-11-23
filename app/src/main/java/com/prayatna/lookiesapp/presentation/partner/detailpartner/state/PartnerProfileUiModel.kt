package com.prayatna.lookiesapp.presentation.partner.detailpartner.state

data class DetailPartnerUiModel(
    val id: Int,
    val name: String,
    val type: String,
    val logoUrl: String,
    val portfolioLink: String,
    val status: String,
    val locations: LocationUiModel,
    val profile: ProfileUiModel
)

data class LocationUiModel (
    val name: String,
    val locUrl: String
)

data class ProfileUiModel (
    val id: Int,
    val profileUrl: String,
    val username: String,
    val fullName: String,
    val address: String,
    val bio: String,
    val hasPartnerSub: Boolean
)