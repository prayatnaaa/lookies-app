package com.prayatna.lookiesapp.domain.usecase.shipment

import android.net.Uri
import com.prayatna.lookiesapp.domain.repository.ShipmentRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UploadExhibitionArrivalProofUseCase @Inject constructor(
    private val shipmentRepository: ShipmentRepository
) {
    suspend operator fun invoke(shipmentId: String, image: Uri): DataResult<String> {
        return shipmentRepository.uploadExhibitionArrivalProof(shipmentId, image)
    }
}
