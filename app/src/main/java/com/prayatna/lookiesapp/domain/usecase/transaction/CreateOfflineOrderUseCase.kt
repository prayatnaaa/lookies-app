package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateOfflineOrderUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        buyerId: String? = null,
        currency: String = "IDR",
        items: List<OrderItemInput>
    ): DataResult<String> {
        return transactionRepository.createOfflineOrder(
            buyerId = buyerId,
            currency = currency,
            items = items
        )
    }
}
