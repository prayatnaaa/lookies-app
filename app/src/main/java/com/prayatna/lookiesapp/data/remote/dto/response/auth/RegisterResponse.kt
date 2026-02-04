package com.prayatna.lookiesapp.data.remote.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val status: String,
    val message: String,
    val data: RegisterResponseData? = null
)

@Serializable
data class RegisterResponseData(
    @SerialName("full_name")
    val fullName: String,
    val email: String
)
