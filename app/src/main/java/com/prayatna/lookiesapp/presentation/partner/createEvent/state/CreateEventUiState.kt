package com.prayatna.lookiesapp.presentation.partner.createEvent.state

data class CreateEventUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
