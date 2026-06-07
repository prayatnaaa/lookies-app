package com.prayatna.lookiesapp.presentation.event.detailevent.state

sealed class DetailEventUiEvent {
    data class ShowResult(val isSuccess: Boolean, val message: String) : DetailEventUiEvent()
    data class NavigateToChat(
        val conversationId: String? = null,
        val merchantId: String? = null,
        val merchantName: String
    ) : DetailEventUiEvent()
}
