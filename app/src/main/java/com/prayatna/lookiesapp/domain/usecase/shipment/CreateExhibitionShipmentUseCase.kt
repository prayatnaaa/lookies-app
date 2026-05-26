package com.prayatna.lookiesapp.domain.usecase.shipment

import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.domain.model.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.model.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateExhibitionShipmentUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) {
    suspend operator fun invoke(input: CreateExhibitionShipmentInput): DataResult<ExhibitionShipment> {
        return shipmentRepository.createExhibitionShipment(input)
    }
}
