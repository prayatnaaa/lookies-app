package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("user_id")
    val id: String? = null,
    @SerialName("bio")
    val bio: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("has_partner_sub")
    val hasPartnerSub: Boolean? = null,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String? = null,
    @SerialName("is_artist")
    val isArtist: Boolean? = false

)