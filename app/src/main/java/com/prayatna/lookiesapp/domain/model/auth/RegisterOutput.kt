package com.prayatna.lookiesapp.domain.model.auth


data class RegisterOutput(
    val status: String,
    val message: String,
    val data: RegisterData? = null
)

data class RegisterData(
    val fullName: String,
    val email: String
)