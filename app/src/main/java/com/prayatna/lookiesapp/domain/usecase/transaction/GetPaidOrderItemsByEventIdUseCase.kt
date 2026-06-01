package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.PaidOrderItem
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPaidOrderItemsByEventIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(eventId: Int): DataResult<List<PaidOrderItem>> {
        return transactionRepository.getPaidOrderItemsByEventId(eventId)
    }
}
