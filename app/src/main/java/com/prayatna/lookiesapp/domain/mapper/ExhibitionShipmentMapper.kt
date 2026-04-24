package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ExhibitionShipmentDto
import com.prayatna.lookiesapp.data.remote.dto.request.shipment.CreateExhibitionShipmentRequest
import com.prayatna.lookiesapp.domain.shipment.DeliveryMethod
import com.prayatna.lookiesapp.domain.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.domain.shipment.ShipmentStatus
import com.prayatna.lookiesapp.domain.shipment.ShipmentType
import java.time.OffsetDateTime
import java.util.UUID

fun ExhibitionShipmentDto.toDomain(): ExhibitionShipment {
    return ExhibitionShipment(
        id = UUID.fromString(id),
        eventPaintingId = UUID.fromString(eventPaintingId),
        artistId = UUID.fromString(artistId),
        organizerId = UUID.fromString(organizerId),
        shipmentType = ShipmentType.valueOf(shipmentType.uppercase()),
        deliveryMethod = DeliveryMethod.valueOf(deliveryMethod.uppercase()),
        courierName = courierName,
        trackingNumber = trackingNumber,
        dispatchedAt = dispatchedAt?.let { OffsetDateTime.parse(it) },
        receivedAt = receivedAt?.let { OffsetDateTime.parse(it) },
        galleryConditionNotes = galleryConditionNotes,
        artistConditionNotes = artistConditionNotes,
        status = ShipmentStatus.valueOf(status.uppercase()),
        createdAt = createdAt?.let { OffsetDateTime.parse(it) },
        eventId = eventId
    )
}

fun ExhibitionShipmentDto.toInsertDto(): CreateExhibitionShipmentRequest {
    return CreateExhibitionShipmentRequest(
        eventPaintingId = eventPaintingId,
        artistId = artistId,
        organizerId = organizerId,
        shipmentType = shipmentType.lowercase(),
        deliveryMethod = deliveryMethod.lowercase(),
        courierName = courierName,
        trackingNumber = trackingNumber,
        dispatchedAt = dispatchedAt,
        receivedAt = receivedAt,
        galleryConditionNotes = galleryConditionNotes,
        artistConditionNotes = artistConditionNotes,
        status = status.lowercase(),
        eventId = eventId
    )
}

fun com.prayatna.lookiesapp.domain.shipment.CreateExhibitionShipmentInput.toDto(): CreateExhibitionShipmentRequest {
    return CreateExhibitionShipmentRequest(
        eventPaintingId = eventPaintingId.toString(),
        artistId = artistId.toString(),
        organizerId = organizerId.toString(),
        shipmentType = shipmentType.name.lowercase(),
        deliveryMethod = deliveryMethod.name.lowercase(),
        courierName = courierName,
        trackingNumber = trackingNumber,
        dispatchedAt = dispatchedAt?.toString(),
        receivedAt = receivedAt?.toString(),
        galleryConditionNotes = galleryConditionNotes,
        artistConditionNotes = artistConditionNotes,
        status = status.name.lowercase(),
        eventId = eventId
    )
}