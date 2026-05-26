package com.prayatna.lookiesapp.presentation.login.state

sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val role: String) : AuthState()
}
