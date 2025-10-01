package com.prayatna.lookiesapp.data.remote.response

sealed interface AuthResponse {
    data object Success: AuthResponse
    data class Error(val message: String?): AuthResponse
}