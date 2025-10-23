package com.prayatna.lookiesapp.data.model

data class User (
    val id: String,
    val username: String,
    val fullName: String? = null,
    val profileUrl: String? = null,
    val address: String? = null,
    val bio: String? = null,
    val role: String
)