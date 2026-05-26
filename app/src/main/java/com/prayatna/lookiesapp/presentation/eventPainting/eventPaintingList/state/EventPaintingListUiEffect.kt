package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state

sealed interface EventPaintingListUiEffect {
    data class ShowToast(val message: String) : EventPaintingListUiEffect
    data object NavigateBack : EventPaintingListUiEffect
    data class NavigateToDetail(val id: String) : EventPaintingListUiEffect
}
