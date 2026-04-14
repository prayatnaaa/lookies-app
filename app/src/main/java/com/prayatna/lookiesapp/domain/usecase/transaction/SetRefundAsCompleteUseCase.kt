package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class SetRefundAsCompleteUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(refundRequestId: String): DataResult<SetRefundAsCompleteResult> {
        return transactionRepository.setRefundAsComplete(refundRequestId)
    }
}
