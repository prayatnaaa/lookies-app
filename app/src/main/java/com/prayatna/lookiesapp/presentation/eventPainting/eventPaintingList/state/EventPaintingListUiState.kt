package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute

data class EventPaintingListUiState(
    val isLoading: Boolean = false,
    val paintings: List<EventPainting> = emptyList(),
    val filteredPaintings: List<EventPainting> = emptyList(),
    val searchQuery: String = "",
    val selectedStatus: String? = "on_sale",
    val selectedArtStyle: String? = null,
    val selectedMedium: String? = null,
    val isPriceAscending: Boolean = true,
    val errorMessage: String? = null,
    val isFilterSheetOpen: Boolean = false,
    val artStyles: List<PaintingAttribute> = emptyList(),
    val mediums: List<PaintingAttribute> = emptyList()
)
