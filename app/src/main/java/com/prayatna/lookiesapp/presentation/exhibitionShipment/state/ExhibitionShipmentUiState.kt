package com.prayatna.lookiesapp.presentation.exhibitionShipment.state

import com.prayatna.lookiesapp.domain.shipment.DeliveryMethod
import com.prayatna.lookiesapp.domain.shipment.ExhibitionShipment

data class ExhibitionShipmentUiState(
    // Loading / error
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Existing shipment (if any)
    val shipment: ExhibitionShipment? = null,

    // Form — Inbound phase (Artist submits artwork)
    val selectedDeliveryMethod: DeliveryMethod = DeliveryMethod.SELF_DROP_OFF,
    val courierNameInput: String = "",
    val trackingNumberInput: String = "",

    // Form — Status update / notes
    val notesInput: String = "",

    // For organizer: mark artwork as received in gallery
    val isMarkingReceived: Boolean = false,

    // For organizer: return shipment setup
    val returnTrackingNumberInput: String = "",

    // Contextual IDs needed to build the shipment
    val eventPaintingId: String = "",
    val artistId: String = "",
    val organizerId: String = "",
    val eventId: Int = 0,

    // Current event painting status (drives which phase/actions are shown)
    val eventPaintingStatus: String = "",

    // ── Display info (shown in the context card at the top of the screen) ──
    val paintingTitle: String = "",
    val eventTitle: String = "",
    val eventLocation: String = "",
    val organizerName: String = "",
    val eventStartDate: String = "",
    val eventEndDate: String = "",
)
