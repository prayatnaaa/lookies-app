package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentAttemptUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(orderId: String): Flow<DataResult<PaymentAttempt>> {
        return transactionRepository.getPaymentAttempt(orderId)
    }
}