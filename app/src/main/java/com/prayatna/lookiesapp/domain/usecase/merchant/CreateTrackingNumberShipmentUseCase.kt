package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateTrackingNumberShipmentUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {

    suspend operator fun invoke(shipmentId: String, trackingNumber: String): DataResult<Shipment> {
        return merchantRepository.createTrackingNumberShipment(shipmentId, trackingNumber)
    }
}
