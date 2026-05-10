package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.PendingOrderSplits
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPendingOrderSplitByMerchantIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(merchantId: String): DataResult<PendingOrderSplits> {
        return transactionRepository.getPendingOrderSplitByMerchantId(merchantId)
    }
}
