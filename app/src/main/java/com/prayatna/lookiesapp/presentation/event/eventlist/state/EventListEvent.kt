package com.prayatna.lookiesapp.presentation.event.eventlist.state

sealed interface EventListEvent {
    data object OnBackClicked : EventListEvent
    data class OnDetailEventClicked(val id: String): EventListEvent
    data class OnSearchQueryChange(val query: String) : EventListEvent
    data object OnSearchTriggered : EventListEvent
    data class OnStatusSelected(val status: String?) : EventListEvent
    data class OnEventTypeSelected(val type: String?) : EventListEvent
    data class OnEventFormatSelected(val format: String?) : EventListEvent
    data class OnLocationChange(val location: String?) : EventListEvent
    data class OnStartDateChange(val date: String?) : EventListEvent
    data class OnEndDateChange(val date: String?) : EventListEvent
    data object OnSortToggled : EventListEvent
    data object OnFilterSheetToggle : EventListEvent
    data object OnApplyFilters : EventListEvent
    data object OnResetFilters : EventListEvent
    data object OnRetry : EventListEvent
}
