package com.prayatna.lookiesapp.domain.model.transaction

import com.prayatna.lookiesapp.domain.model.shipment.Shipment

data class DetailPaintingOrder (
    val transaction: Transaction,
    val shipment: Shipment
)