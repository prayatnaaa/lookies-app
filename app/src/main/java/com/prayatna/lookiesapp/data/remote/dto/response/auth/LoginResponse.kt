package com.prayatna.lookiesapp.data.remote.dto.response.auth

data class LoginResponse (
    val success: Boolean,
    val role: String,
    val message: String? = null
)