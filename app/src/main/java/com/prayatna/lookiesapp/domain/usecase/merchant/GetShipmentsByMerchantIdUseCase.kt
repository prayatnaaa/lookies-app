package com.prayatna.lookiesapp.domain.usecase.merchant

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetShipmentsByMerchantIdUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {
    suspend operator fun invoke(merchantId: String): DataResult<List<Shipment>> {
        return merchantRepository.getShipmentsByMerchantId(merchantId)
    }
}