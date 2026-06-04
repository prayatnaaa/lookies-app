package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetRefundByIdUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(id: String): DataResult<DetailRefund> {
        return refundRepository.getRefundById(id)
    }
}