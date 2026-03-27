package com.prayatna.lookiesapp.presentation.registerEvent.state

import com.prayatna.lookiesapp.domain.model.artist.RegisterEventOutput
import com.prayatna.lookiesapp.domain.model.painting.Painting

data class RegisterEventUiState(
    val isLoading: Boolean = false,
    val currentStep: Int = 1,
    val eventId: Int = 0,
    val maxLimit: Int = 3,
    val allPaintings: List<Painting> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val data: RegisterEventOutput? = null
)
