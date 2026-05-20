package com.prayatna.lookiesapp.presentation.partner.editEvent.state

import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType

data class EditEventFormState(
    val bannerImage: String = "",
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val location: String = "",
    val locationUrl: String = "",
    val maxParticipant: String? = null,
    val maxPainting: String? = null,
    val maxPaintingPerArtist: String? = null,
    val about: String = "",
    val ticketPrice: String? = null,
    val artistRegistrationFee: String? = null,
    val eventType: String = "",
    val eventFormat: String = "",
    val eventTypes: List<TEventType> = emptyList(),
    val eventFormats: List<EventFormat> = emptyList(),
    val isLoadingMeta: Boolean = false,
    val errorMessage: String? = null,
    val status: String? = null
)