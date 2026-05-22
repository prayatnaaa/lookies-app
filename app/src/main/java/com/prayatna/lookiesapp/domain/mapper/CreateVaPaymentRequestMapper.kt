package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.payment.CreateVaPaymentRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.CreateVaPaymentResponse
import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestInput
import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestResult

fun CreateVaPaymentRequestInput.toDto(): CreateVaPaymentRequest {
    return CreateVaPaymentRequest(
        merchantId = merchantId,
        orderId = orderId,
        amount = amount,
        channelCode = channelCode,
        customerName = customerName,
        expiresAt = expiresAt,
        description = description
    )
}

fun CreateVaPaymentResponse.toDomain(): CreateVaPaymentRequestResult {
    return CreateVaPaymentRequestResult(
        status = status,
        message = message,
        paymentRequestId = data?.paymentRequestId,
        paymentMethodId = data?.paymentMethodId,
        virtualAccountNumber = data?.virtualAccountNumber,
        expiresAt = data?.expiresAt
    )
}
