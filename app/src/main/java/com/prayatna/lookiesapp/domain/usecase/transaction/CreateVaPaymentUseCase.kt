package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestResult
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CreateVaPaymentUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(input: CreateVaPaymentRequestInput): DataResult<CreateVaPaymentRequestResult> {
        val expiresAt = Instant.now()
            .plus(5, ChronoUnit.MINUTES)
            .toString()
        return repository.createVaPaymentRequest(input.copy(expiresAt = expiresAt))
    }
}
