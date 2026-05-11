package com.prayatna.lookiesapp.domain.usecase.merchant

import android.net.Uri
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UploadShipmentArrivalProofUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository
) {
    suspend operator fun invoke(shipmentId: String, image: Uri): DataResult<String> {
        return merchantRepository.uploadShipmentArrivalProof(shipmentId, image)
    }
}
