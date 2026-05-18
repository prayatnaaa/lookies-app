package com.prayatna.lookiesapp.presentation.admin.detailEvent.state

sealed interface AdminDetailEventUiEvent {
    data class LoadDetail(val eventId: String) : AdminDetailEventUiEvent
    data class ApproveEvent(val eventId: Int) : AdminDetailEventUiEvent
    data class RejectEvent(val eventId: Int, val reason: String) : AdminDetailEventUiEvent
    data object ClearError : AdminDetailEventUiEvent
}
