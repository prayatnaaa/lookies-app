package com.prayatna.lookiesapp.domain.shipment

import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import javax.inject.Inject

class GetShipmentFeeUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) {
    suspend operator fun invoke() =
        shipmentRepository.getShipmentFees()
}