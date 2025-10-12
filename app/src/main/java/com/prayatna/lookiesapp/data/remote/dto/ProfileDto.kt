package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto (

    @SerialName("user_id")
    val id: String,

    @SerialName("profile_picture_url")
    val profileUrl: String?,

    @SerialName("username")
    val username: String?,

    @SerialName("full_name")
    val fullName: String?,

    @SerialName("address")
    val address: String?,

    @SerialName("bio")
    val bio: String?,

)