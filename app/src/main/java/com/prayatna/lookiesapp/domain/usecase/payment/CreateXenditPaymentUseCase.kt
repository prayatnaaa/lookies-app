package com.prayatna.lookiesapp.domain.usecase.payment

import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateXenditPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateXenditPaymentResponse
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateXenditPaymentUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(data: CreateXenditPaymentRequest): DataResult<CreateXenditPaymentResponse> {
        val result = transactionRepository.createPayment(request = data)
        return result
    }
}