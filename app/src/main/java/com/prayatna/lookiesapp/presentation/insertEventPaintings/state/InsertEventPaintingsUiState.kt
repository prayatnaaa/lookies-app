package com.prayatna.lookiesapp.presentation.insertEventPaintings.state

import com.prayatna.lookiesapp.domain.model.painting.Painting

data class InsertEventPaintingsUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val availablePaintings: List<Painting> = emptyList(),
    val selectedPaintings: Set<Painting> = emptySet()
)