package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.presentation.partner.editEvent.state.EditEventFormState

fun EditEventFormState.toEditEventInput(): EditEventInput {
    return EditEventInput(
        title = title,
        startDate = startDate,
        endDate = endDate,
        about = about.ifBlank { null },
        location = location,
        locationUrl = locationUrl,
        maxParticipant = maxParticipant?.toIntOrNull(),
        maxPainting = maxPainting?.toIntOrNull(),
        maxPaintingPerArtist = maxPaintingPerArtist?.toIntOrNull(),
        ticketPrice = ticketPrice?.toDoubleOrNull(),
        registrationFee = artistRegistrationFee?.toDoubleOrNull(),
        eventType = eventType.toInt(),
        eventFormat = eventFormat.toInt()
    )
}
