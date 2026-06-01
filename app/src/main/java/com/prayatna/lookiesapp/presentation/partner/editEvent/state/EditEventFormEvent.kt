package com.prayatna.lookiesapp.presentation.partner.editEvent.state

sealed interface EditEventFormEvent {
    data class TitleChanged(val value: String) : EditEventFormEvent
    data class StartDateChanged(val value: String) : EditEventFormEvent
    data class EndDateChanged(val value: String) : EditEventFormEvent
    data class LocationChanged(val value: String) : EditEventFormEvent
    data class LocationUrlChanged(val value: String) : EditEventFormEvent
    data class MaxParticipantChanged(val value: String?) : EditEventFormEvent
    data class MaxPaintingChanged(val value: String?) : EditEventFormEvent
    data class MaxPaintingPerArtistChanged(val value: String?) : EditEventFormEvent
    data class AboutChanged(val value: String) : EditEventFormEvent
    data class TicketPriceChanged(val value: String?) : EditEventFormEvent
    data class ArtistRegistrationFeeChanged(val value: String?) : EditEventFormEvent
    data class EventTypeChanged(val value: String) : EditEventFormEvent
    data class PaintingSubmissionDeadline(val value: String): EditEventFormEvent
    data class EventFormatChanged(val value: String) : EditEventFormEvent
    data object LoadEventMeta : EditEventFormEvent
    data object Submit : EditEventFormEvent
}
