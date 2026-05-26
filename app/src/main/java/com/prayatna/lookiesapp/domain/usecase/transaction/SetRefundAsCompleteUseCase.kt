package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class SetRefundAsCompleteUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(refundRequestId: String): DataResult<SetRefundAsCompleteResult> {
        return refundRepository.setRefundAsComplete(refundRequestId)
    }
}
