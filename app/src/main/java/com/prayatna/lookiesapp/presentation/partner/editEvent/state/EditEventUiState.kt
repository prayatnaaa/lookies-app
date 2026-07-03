package com.prayatna.lookiesapp.presentation.partner.editEvent.state

import com.prayatna.lookiesapp.domain.model.event.Event

data class EditEventUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isDeleteSuccess: Boolean = false,
    val data: Event? = null
)