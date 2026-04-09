package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetShipmentFeeUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke() =
        transactionRepository.getShipmentFees()
}