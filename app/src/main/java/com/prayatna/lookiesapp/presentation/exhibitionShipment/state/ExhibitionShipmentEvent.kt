package com.prayatna.lookiesapp.presentation.exhibitionShipment.state

import com.prayatna.lookiesapp.domain.shipment.DeliveryMethod

sealed class ExhibitionShipmentEvent {

    // ── Inbound: Artist submits shipment details ──────────────────────────────
    /** Artist selects how they will deliver the artwork */
    data class OnDeliveryMethodSelected(val method: DeliveryMethod) : ExhibitionShipmentEvent()

    /** Artist fills in courier name (only needed for COURIER method) */
    data class OnCourierNameChanged(val name: String) : ExhibitionShipmentEvent()

    /** Artist fills in their inbound tracking number */
    data class OnTrackingNumberChanged(val trackingNumber: String) : ExhibitionShipmentEvent()

    /** Artist submits shipment → creates ExhibitionShipment + status → shipping_to_event */
    data object SubmitInboundShipment : ExhibitionShipmentEvent()

    // ── Inbound: Organizer confirms artwork received ──────────────────────────
    /** Organizer confirms artwork arrived at gallery → status → exhibited */
    data class OnGalleryNotesChanged(val notes: String) : ExhibitionShipmentEvent()
    data object ConfirmArtworkReceived : ExhibitionShipmentEvent()

    // ── Outbound: Organizer arranges return shipment ──────────────────────────
    /** Organizer enters return tracking number */
    data class OnReturnTrackingNumberChanged(val trackingNumber: String) : ExhibitionShipmentEvent()

    /** Organizer submits return shipment → creates ExhibitionShipment (RETURN) + status → returning_to_creator */
    data object SubmitReturnShipment : ExhibitionShipmentEvent()

    // ── Outbound: Artist confirms receipt ────────────────────────────────────
    /** Artist confirms they received the artwork back → status → returned */
    data class OnArtistConditionNotesChanged(val notes: String) : ExhibitionShipmentEvent()
    data object ConfirmArtworkReturned : ExhibitionShipmentEvent()

    // ── Generic ───────────────────────────────────────────────────────────────
    data object DismissError : ExhibitionShipmentEvent()
    data object DismissSuccess : ExhibitionShipmentEvent()
    data object OnBackClick : ExhibitionShipmentEvent()
}
