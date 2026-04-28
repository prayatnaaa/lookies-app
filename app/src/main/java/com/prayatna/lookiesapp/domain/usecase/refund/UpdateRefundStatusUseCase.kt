package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateRefundStatusUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(id: String, status: String, note: String? = null): DataResult<Refund> {

        if (status != "completed" && note == null) {
            return DataResult.Error("Note is required")
        }
        return refundRepository.updateRefundStatus(id = id, status = status, note)
    }
}