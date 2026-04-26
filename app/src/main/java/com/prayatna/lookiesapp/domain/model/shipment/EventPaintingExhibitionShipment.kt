package com.prayatna.lookiesapp.domain.model.shipment

import com.prayatna.lookiesapp.domain.model.painting.EventPainting

data class EventPaintingExhibitionShipment(
    val exhibitionShipment: ExhibitionShipment,
    val eventPainting: EventPainting
)