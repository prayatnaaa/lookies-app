package com.prayatna.lookiesapp.domain.shipment

import java.time.OffsetDateTime
import java.util.UUID

data class CreateExhibitionShipmentInput(
    val eventPaintingId: UUID,
    val artistId: UUID,
    val organizerId: UUID,
    val shipmentType: ShipmentType,
    val deliveryMethod: DeliveryMethod,
    val courierName: String? = null,
    val trackingNumber: String? = null,
    val dispatchedAt: OffsetDateTime? = null,
    val receivedAt: OffsetDateTime? = null,
    val galleryConditionNotes: String? = null,
    val artistConditionNotes: String? = null,
    val status: ShipmentStatus = ShipmentStatus.PENDING,
    val eventId: Int
)
