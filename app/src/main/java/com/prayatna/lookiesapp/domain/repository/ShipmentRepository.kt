package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.utils.DataResult

interface ShipmentRepository  {
    suspend fun getShipmentByOrderId(orderId: String):
            DataResult<Shipment>
    suspend fun getShipmentFees():
            DataResult<List<ShipmentFee>>

    suspend fun createExhibitionShipment(input: com.prayatna.lookiesapp.domain.shipment.CreateExhibitionShipmentInput): DataResult<com.prayatna.lookiesapp.domain.shipment.ExhibitionShipment>

    suspend fun updateExhibitionShipmentStatus(shipmentId: String, notes: String?, status: String): DataResult<com.prayatna.lookiesapp.domain.shipment.ExhibitionShipment>
}