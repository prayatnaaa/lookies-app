package com.prayatna.lookiesapp.domain.model.shipment

import java.time.OffsetDateTime
import java.util.UUID

data class ExhibitionShipment(
    val id: UUID,
    val eventPaintingId: UUID,
    val artistId: UUID,
    val organizerId: UUID,
    val shipmentType: ShipmentType,
    val deliveryMethod: DeliveryMethod,
    val courierName: String?,
    val trackingNumber: String?,
    val dispatchedAt: OffsetDateTime?,
    val receivedAt: OffsetDateTime?,
    val galleryConditionNotes: String?,
    val artistConditionNotes: String?,
    val status: ShipmentStatus,
    val createdAt: OffsetDateTime?,
    val eventId: Int,
    val arrivalProofUrl: String? = null
)

enum class ShipmentType {
    INBOUND,
    RETURN
}

enum class DeliveryMethod {
    SELF_DROP_OFF,
    COURIER,
    ARTIST_PICKUP
}

enum class ShipmentStatus {
    PENDING,
    ON_THE_WAY,
    RECEIVED_IN_GALLERY,
    READY_FOR_PICKUP,
    RETURNED_TO_ARTIST,
    PROBLEM
}