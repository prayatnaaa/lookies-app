package com.prayatna.lookiesapp.presentation.components

sealed class SessionState {
    data object Loading : SessionState()
    data object Authenticated : SessionState()
    data object Unauthenticated : SessionState()
    data class Error(val message: String? = null) : SessionState()
}
