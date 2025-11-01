package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleDto(
    @SerialName("user_id")
    val userId: String,
    val email: String,
    @SerialName("role_id")
    val roleId: Int,
    @SerialName("role_name")
    val roleName: String
)
