package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: String,
    val username: String,
    val role: String
)