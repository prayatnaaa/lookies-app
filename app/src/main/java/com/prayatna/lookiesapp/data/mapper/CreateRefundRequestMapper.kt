package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateRefundRequest
import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import kotlinx.datetime.Clock

fun CreateRefundRequestInput.toDto(): CreateRefundRequest {
    val now = Clock.System.now()
    return CreateRefundRequest(
        orderId = orderId,
        userId = userId,
        amount = amount,
        bankCode = bankCode,
        accountNumber = accountNumber,
        accountHolderName = accountHolderName,
        reason = reason,
        proofImageUrl = proofImageUrl,
        returnTrackingNumber = returnTrackingNumber,
        adminNotes = adminNotes,
        xenditPayoutId = xenditPayoutId,
        createdAt = now,
        updatedAt = now
    )
}
