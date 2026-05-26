package com.prayatna.lookiesapp.domain.model.auth

data class LoginOutput (
    val success: Boolean,
    val role: String,
    val message: String? = null
)