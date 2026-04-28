package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetRefundByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(id: String) = transactionRepository.getRefundById(id)
}