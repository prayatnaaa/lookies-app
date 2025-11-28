package com.prayatna.lookiesapp.presentation.register.events

sealed class RegisterEvent {
    data class ShowSnackbar(val message: String) : RegisterEvent()
    data object NavigateToLogin : RegisterEvent()
}
