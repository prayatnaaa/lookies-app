package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state

sealed class EventPaintingDetailEvent {

    data class Load(val id: String) : EventPaintingDetailEvent()

    data class Approve(val id: String) : EventPaintingDetailEvent()

    data class Reject(
        val id: String,
        val reason: String
    ) : EventPaintingDetailEvent()
}