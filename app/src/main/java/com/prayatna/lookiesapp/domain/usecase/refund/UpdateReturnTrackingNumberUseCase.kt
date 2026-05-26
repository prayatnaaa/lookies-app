package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateReturnTrackingNumberUseCase @Inject constructor(
    private val repository: RefundRepository
) {
    suspend operator fun invoke(id: String, trackingNumber: String): DataResult<Refund> {
        return repository.updateReturnTrackingNumber(id, trackingNumber)
    }
}
