package com.prayatna.lookiesapp.presentation.painting.paintinglist.state

import com.prayatna.lookiesapp.domain.model.painting.Painting

data class PaintingUiState(
    val paintings: List<Painting> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
