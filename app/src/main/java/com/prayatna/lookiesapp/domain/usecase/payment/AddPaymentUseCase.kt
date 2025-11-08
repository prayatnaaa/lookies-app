package com.prayatna.lookiesapp.domain.usecase.payment

import com.prayatna.lookiesapp.domain.model.payment.AddPayment
import com.prayatna.lookiesapp.domain.model.payment.AddPaymentResult
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class AddPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(request: AddPayment): DataResult<AddPaymentResult> {
        return paymentRepository.addPayment(request)
    }
}