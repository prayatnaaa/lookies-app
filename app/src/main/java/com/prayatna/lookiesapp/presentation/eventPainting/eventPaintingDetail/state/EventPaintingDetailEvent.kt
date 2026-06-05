package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state

sealed class EventPaintingDetailEvent {
    data class NavigateToChat(val conversationId: String) : EventPaintingDetailEvent()
}