package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.ShipmentDto
import com.prayatna.lookiesapp.domain.model.transaction.Shipment

fun ShipmentDto.toDomain() = Shipment(
    id = id,
    merchantId = merchantId,
    orderId = orderId,
    trackingNumber = trackingNumber,
    status = status,
    shippingCost = shippingCost,
    createdAt = createdAt,
    shippedAt = shippedAt,
)