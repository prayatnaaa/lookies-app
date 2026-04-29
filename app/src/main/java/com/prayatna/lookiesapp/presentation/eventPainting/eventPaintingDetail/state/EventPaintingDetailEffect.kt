package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state

sealed class EventPaintingDetailEffect {

    data class ShowResult(val message: String) : EventPaintingDetailEffect()

    data class ShowError(val message: String) : EventPaintingDetailEffect()
}