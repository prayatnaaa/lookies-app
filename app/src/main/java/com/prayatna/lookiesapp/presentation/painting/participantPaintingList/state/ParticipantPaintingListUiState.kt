package com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state

import com.prayatna.lookiesapp.domain.model.painting.EventPainting


data class ParticipantPaintingListUiState(
    val isLoading: Boolean = false,
    val eventPaintings: List<EventPainting> = emptyList(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val loadingPaintingId: String? = null,
    val dialogState: DialogState? = null
)

sealed class DialogState {
    data class Approve(val paintingId: String) : DialogState()
    data class Reject(val paintingId: String) : DialogState()
}

