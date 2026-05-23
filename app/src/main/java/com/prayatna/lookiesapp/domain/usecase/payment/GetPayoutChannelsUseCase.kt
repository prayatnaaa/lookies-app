package com.prayatna.lookiesapp.domain.usecase.payment

import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel
import com.prayatna.lookiesapp.domain.repository.PaymentRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetPayoutChannelsUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(): DataResult<List<PayoutChannel>> {
        return repository.getPayoutChannels()
    }
}
