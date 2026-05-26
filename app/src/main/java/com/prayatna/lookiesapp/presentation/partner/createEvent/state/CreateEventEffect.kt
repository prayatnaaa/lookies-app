package com.prayatna.lookiesapp.presentation.partner.createEvent.state

sealed class CreateEventEffect {
    data object NavigateBack : CreateEventEffect()
    data class ShowError(val message: String) : CreateEventEffect()
}