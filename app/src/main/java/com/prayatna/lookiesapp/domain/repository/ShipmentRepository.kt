package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.domain.model.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.model.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.utils.DataResult

interface ShipmentRepository  {
    suspend fun getShipmentByOrderId(orderId: String):
            DataResult<Shipment>
    suspend fun getShipmentFees():
            DataResult<List<ShipmentFee>>

    suspend fun createExhibitionShipment(input: CreateExhibitionShipmentInput):
            DataResult<ExhibitionShipment>

    suspend fun updateExhibitionShipmentStatus(shipmentId: String, notes: String?, status: String):
            DataResult<ExhibitionShipment>

    suspend fun getExhibitionShipmentByEventPaintingId(eventPaintingId: String):
            DataResult<ExhibitionShipment?>
}