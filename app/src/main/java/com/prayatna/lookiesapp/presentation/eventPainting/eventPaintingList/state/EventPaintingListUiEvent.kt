package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state

sealed interface EventPaintingListUiEvent {
    data class OnSearchQueryChange(val query: String) : EventPaintingListUiEvent
    data object OnSearchTriggered : EventPaintingListUiEvent
    data class OnStatusSelected(val status: String?) : EventPaintingListUiEvent
    data class OnArtStyleSelected(val style: String?) : EventPaintingListUiEvent
    data class OnMediumSelected(val medium: String?) : EventPaintingListUiEvent
    data object OnSortPriceToggled : EventPaintingListUiEvent
    data object OnFilterSheetToggle : EventPaintingListUiEvent
    data object OnApplyFilters : EventPaintingListUiEvent
    data object OnResetFilters : EventPaintingListUiEvent
    data object OnRetry : EventPaintingListUiEvent
}
