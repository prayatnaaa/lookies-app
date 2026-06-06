package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt

fun PaymentAttemptDto.toDomain(): PaymentAttempt {
    val pm = rawResponse?.paymentMethod
    val vaInfo = pm?.virtualAccount?.channelProperties
    val qrInfo = pm?.qrCode?.channelProperties
    
    return PaymentAttempt(
        id = id,
        orderId = orderId,
        provider = provider,
        channel = channel,
        externalId = externalId,
        amount = amount,
        currency = currency,
        status = status,
        failureReason = failureReason,
        createdAt = createdAt,
        updatedAt = updatedAt,
        paymentRequestId = paymentRequestId,
        redirectUrl = redirectUrl,
        virtualAccountNumber = vaInfo?.virtualAccountNumber,
        qrString = qrInfo?.qrString,
        customerName = vaInfo?.customerName,
        expiresAt = vaInfo?.expiresAt ?: qrInfo?.expiresAt
    )
}
