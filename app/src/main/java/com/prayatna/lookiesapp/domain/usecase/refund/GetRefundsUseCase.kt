package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetRefundsUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(): DataResult<List<Refund>> {
        return refundRepository.getRefunds()
    }
}
