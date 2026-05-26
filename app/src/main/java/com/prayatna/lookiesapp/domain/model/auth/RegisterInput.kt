package com.prayatna.lookiesapp.domain.model.auth

data class RegisterInput(
    val fullName: String,
    val email: String,
    val password: String,
    val verifyPassword: String
)