package com.prayatna.lookiesapp.domain.usecase.event

import com.prayatna.lookiesapp.domain.model.event.DefaultEvent
import com.prayatna.lookiesapp.domain.model.event.EditEventInput
import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

private fun validateEditEvent(input: EditEventInput): String? {

    if (input.title.isBlank()) return "Event title cannot be empty"

    if (input.startDate.isBlank()) return "Start date cannot be empty"
    if (input.endDate.isBlank()) return "End date cannot be empty"

    if (input.location.isBlank()) return "Location cannot be empty"

    input.maxParticipant?.let {
        if (it <= 0) return "Maximum participants must be greater than 0"
    }

    input.maxPainting?.let {
        if (it <= 0) return "Maximum paintings must be greater than 0"
    }

    input.maxPaintingPerArtist?.let {
        if (it <= 0) return "Max paintings per artist must be greater than 0"
    }

    input.ticketPrice?.let {
        if (it < 0) return "Ticket price cannot be negative"
    }

    input.registrationFee?.let {
        if (it < 0) return "Registration fee cannot be negative"
    }

    if (input.eventType <= 0) return "Invalid event type"
    if (input.eventFormat <= 0) return "Invalid event format"

    return null
}

class EditEventUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {

    suspend operator fun invoke(
        id: String,
        input: EditEventInput
    ): DataResult<DefaultEvent> {

        val validationError = validateEditEvent(input)
        if (validationError != null) {
            return DataResult.Error(validationError)
        }

        return partnerRepository.updateEvent(
            id = id,
            input = input
        )
    }
}