package com.prayatna.lookiesapp.presentation.painting.detailpainting.state

import com.prayatna.lookiesapp.domain.model.painting.DetailPainting

data class DetailPaintingUiState(
    val painting: DetailPainting? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
