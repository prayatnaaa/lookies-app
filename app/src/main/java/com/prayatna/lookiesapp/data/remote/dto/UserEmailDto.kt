package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserEmailDto(
    val id: String,
    val email: String
)