package com.prayatna.lookiesapp.domain.usecase.refund

import com.prayatna.lookiesapp.domain.repository.RefundRepository
import javax.inject.Inject

class GetRefundByIdUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(id: String) = refundRepository.getRefundById(id)
}