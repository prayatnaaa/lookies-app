package com.prayatna.lookiesapp.presentation.partner.selfEventList.state

sealed interface SelfEventListUiEvent {
    data class OnSearchQueryChange(val query: String) : SelfEventListUiEvent
    data object OnSearchTriggered : SelfEventListUiEvent
    data class OnStatusSelected(val status: String?) : SelfEventListUiEvent
    data class OnFormatSelected(val id: Int?) : SelfEventListUiEvent
    data class OnTypeSelected(val id: Int?) : SelfEventListUiEvent
    data object OnFilterSheetToggle : SelfEventListUiEvent
    data object OnApplyFilters : SelfEventListUiEvent
    data object OnResetFilters : SelfEventListUiEvent
    data object OnRetry : SelfEventListUiEvent
}
