package com.prayatna.lookiesapp.domain.model.user

data class Profile (
    val id: String?,
    val profileUrl: String?,
    val username: String?,
    val fullName: String?,
    val address: String?,
    val bio: String?,
    val hasPartnerSub: Boolean?,
    val isArtist: Boolean?
)