package com.prayatna.lookiesapp.presentation.event.detailevent.state

sealed class DetailEventUiEvent {
    data class ShowResult(val isSuccess: Boolean, val message: String) : DetailEventUiEvent()
}