package com.prayatna.lookiesapp.presentation.partner.editEvent.state

data class EditEventUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)