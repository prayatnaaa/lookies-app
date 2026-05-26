package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.refund.ProcessRefundOutput
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class ProcessRefundUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(refundRequestId: String): DataResult<ProcessRefundOutput> {
        return refundRepository.processRefund(refundRequestId)
    }
}