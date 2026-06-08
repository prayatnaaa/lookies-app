package com.prayatna.lookiesapp.presentation.partner.createEvent.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType

data class CreateEventFormState(
    val title: String = "",
    val bannerUri: Uri? = null,
    val startDate: String = "",
    val endDate: String = "",
    val paintingSubmissionDeadline: String? = null,
    val registrationStartDate: String? = null,
    val registrationEndDate: String? = null,
    val location: String = "",
    val locationUrl: String = "",
    val maxPainting: String? = null,
    val maxPaintingPerArtist: String? = null,
    val about: String = "",
    val ticketPrice: String? = null,
    val artistRegistrationFee: String? = null,
    val eventType: String = "",
    val eventFormat: String = "",
    val eventTypes: List<TEventType> = emptyList(),
    val eventFormats: List<EventFormat> = emptyList(),
    val paintingArtistPercent: Int = 0,
    val paintingEventPercent: Int = 0,
    val paintingPlatformPercent: Int? = null,
    val ticketArtistPercent: Int? = null,
    val ticketEventPercent: Int? = null,
    val ticketPlatformPercent: Int? = null,
    val isLoadingMeta: Boolean = false,
    val errorMessage: String? = null,
) {
    val isValid: Boolean
        get() {
            val selectedType = eventTypes.find { it.id.toString() == eventType }
            val isOpenCall = selectedType?.slug == "open_call"
            
            return title.isNotBlank() &&
            bannerUri != null &&
            startDate.isNotBlank() &&
            endDate.isNotBlank() &&
            eventType.toIntOrNull() != null &&
            eventFormat.toIntOrNull() != null &&
            (!isOpenCall || (
                !paintingSubmissionDeadline.isNullOrBlank() && 
                !registrationStartDate.isNullOrBlank() && 
                !registrationEndDate.isNullOrBlank()
            ))
        }
}
