package com.prayatna.lookiesapp.domain.usecase.shipment

import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.domain.model.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateExhibitionShipmentStatusUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) {
    suspend operator fun invoke(
        shipmentId: String,
        notes: String?,
        status: String
    ): DataResult<ExhibitionShipment> {
        return shipmentRepository.updateExhibitionShipmentStatus(shipmentId, notes, status)
    }
}
