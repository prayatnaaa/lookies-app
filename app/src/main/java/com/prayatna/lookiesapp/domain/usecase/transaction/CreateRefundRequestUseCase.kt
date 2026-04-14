package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateRefundRequestUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(request: CreateRefundRequestInput, proofImage: ByteArray?): DataResult<Refund> {
        return transactionRepository.createRefundRequest(request, proofImage)
    }
}
