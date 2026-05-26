package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteInput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import javax.inject.Inject

class SetOrderToCompleteUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(input: SetOrderToCompleteInput) =
        transactionRepository.setOrderToComplete(input)
}