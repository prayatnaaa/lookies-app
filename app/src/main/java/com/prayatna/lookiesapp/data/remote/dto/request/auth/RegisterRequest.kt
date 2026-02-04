package com.prayatna.lookiesapp.data.remote.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    val password: String,
    @SerialName("verify_password")
    val verifyPassword: String
)