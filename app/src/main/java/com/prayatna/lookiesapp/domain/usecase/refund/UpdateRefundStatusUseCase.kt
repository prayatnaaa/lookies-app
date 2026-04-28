package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class UpdateRefundStatusUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: String, status: String, note: String? = null): DataResult<Refund> {

        if (status != "completed" && note == null) {
            return DataResult.Error("Note is required")
        }
        return transactionRepository.updateRefundStatus(id = id, status = status, note)
    }
}