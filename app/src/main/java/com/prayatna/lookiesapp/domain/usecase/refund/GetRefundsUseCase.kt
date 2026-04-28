package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetRefundsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): DataResult<List<Refund>> {
        return transactionRepository.getRefunds()
    }
}
