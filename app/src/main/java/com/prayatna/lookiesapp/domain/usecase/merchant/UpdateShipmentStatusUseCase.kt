package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateShipmentStatusUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {

    suspend operator fun invoke(shipmentId: String, status: String): DataResult<Shipment> {
        return merchantRepository.updateShipmentStatus(shipmentId, status)
    }
}
