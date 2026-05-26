package com.prayatna.lookiesapp.domain.usecase.shipment

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetShipmentByOrderIdUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) {
    suspend operator fun invoke(orderId: String): DataResult<Shipment> {
        return shipmentRepository.getShipmentByOrderId(orderId)
    }

}