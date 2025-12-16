package com.prayatna.lookiesapp.presentation.partner.createEvent.state

import android.net.Uri

data class CreateEventFormState(
    val title: String = "",
    val bannerUri: Uri? = null,
    val startDate: String = "",
    val endDate: String = "",
    val location: String = "",
    val locationUrl: String = "",
    val maxParticipant: String = "",
    val maxPainting: String = "",
    val maxPaintingPerArtist: String = "",
    val about: String = "",
    val ticketPrice: String = "",
    val artistRegistrationFee: String = ""
) {
    val isValid: Boolean
        get() =
            title.isNotBlank() &&
                    bannerUri != null &&
                    startDate.isNotBlank() &&
                    endDate.isNotBlank() &&
                    location.isNotBlank() &&
                    locationUrl.isNotBlank() &&
                    maxParticipant.toIntOrNull() != null &&
                    maxPainting.toIntOrNull() != null &&
                    maxPaintingPerArtist.toIntOrNull() != null
}