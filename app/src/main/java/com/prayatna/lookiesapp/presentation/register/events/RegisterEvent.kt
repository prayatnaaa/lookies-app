package com.prayatna.lookiesapp.presentation.register.events

sealed class RegisterEvent {
    data class ShowSuccessDialog(val message: String) : RegisterEvent()
    data class ShowErrorDialog(val message: String) : RegisterEvent()
}
