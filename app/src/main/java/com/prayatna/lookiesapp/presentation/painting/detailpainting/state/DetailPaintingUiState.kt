package com.prayatna.lookiesapp.presentation.painting.detailpainting.state

import com.prayatna.lookiesapp.domain.model.painting.Painting

data class DetailPaintingUiState(
    val painting: Painting? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
