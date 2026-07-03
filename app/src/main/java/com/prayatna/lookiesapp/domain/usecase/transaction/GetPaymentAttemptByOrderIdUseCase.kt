package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPaymentAttemptByOrderIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(orderId: String): DataResult<PaymentAttempt?> {
        return repository.getPaymentAttemptByOrderId(orderId)
    }
}
