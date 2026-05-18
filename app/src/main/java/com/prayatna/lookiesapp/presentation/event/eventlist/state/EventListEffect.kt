package com.prayatna.lookiesapp.presentation.event.eventlist.state

sealed interface EventListEffect {
    data class NavigateToDetail(val eventId: String) : EventListEffect
    data object NavigateBack : EventListEffect
}