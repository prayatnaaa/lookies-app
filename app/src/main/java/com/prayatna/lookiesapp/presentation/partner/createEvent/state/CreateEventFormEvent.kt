package com.prayatna.lookiesapp.presentation.partner.createEvent.state

import android.net.Uri

sealed interface CreateEventFormEvent {
    data class TitleChanged(val value: String) : CreateEventFormEvent
    data class BannerChanged(val uri: Uri?) : CreateEventFormEvent
    data class StartDateChanged(val value: String) : CreateEventFormEvent
    data class EndDateChanged(val value: String) : CreateEventFormEvent
    data class LocationChanged(val value: String) : CreateEventFormEvent
    data class LocationUrlChanged(val value: String) : CreateEventFormEvent
    data class MaxParticipantChanged(val value: String) : CreateEventFormEvent
    data class MaxPaintingChanged(val value: String) : CreateEventFormEvent
    data class MaxPaintingPerArtistChanged(val value: String) : CreateEventFormEvent
    data class AboutChanged(val value: String) : CreateEventFormEvent
    data class TicketPriceChanged(val value: String) : CreateEventFormEvent
    data class ArtistRegistrationFeeChanged(val value: String) : CreateEventFormEvent
    data class EventTypeChanged(val value: String) : CreateEventFormEvent
    data class EventFormatChanged(val value: String) : CreateEventFormEvent
    data object LoadEventMeta : CreateEventFormEvent
    data object Submit : CreateEventFormEvent
}
