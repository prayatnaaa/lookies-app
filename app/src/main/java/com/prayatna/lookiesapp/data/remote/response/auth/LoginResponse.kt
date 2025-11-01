package com.prayatna.lookiesapp.data.remote.response.auth

data class LoginResponse (
    val success: Boolean,
    val role: String,
    val message: String? = null
)