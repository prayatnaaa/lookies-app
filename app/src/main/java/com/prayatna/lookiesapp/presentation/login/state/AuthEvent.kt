package com.prayatna.lookiesapp.presentation.login.state

sealed class AuthEvent {
    data class ShowError(val message: String) : AuthEvent()
}