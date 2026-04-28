package com.prayatna.lookiesapp.domain.usecase.transaction

import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.repository.RefundRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateRefundRequestUseCase @Inject constructor(
    private val refundRepository: RefundRepository
) {
    suspend operator fun invoke(request: CreateRefundRequestInput, proofImage: ByteArray?): DataResult<Refund> {
        if (request.orderId.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (request.amount.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (request.bankCode.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (request.accountNumber.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (request.accountHolderName.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (request.reason.isBlank()) {
            return DataResult.Error("Order ID is required")
        }
        if (proofImage == null) {
            return DataResult.Error("Proof image is required")
        }
        if (proofImage.isEmpty()) {
            return DataResult.Error("Proof image is required")
        }

        return refundRepository.createRefundRequest(request, proofImage)
    }
}
