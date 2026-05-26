package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.PaymentAttemptDto
import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt

fun PaymentAttemptDto.toDomain() = PaymentAttempt(
    id = this.id,
    orderId = this.orderId,
    provider = this.provider,
    status = this.status,
    channel = this.channel,
    externalId = this.externalId,
    createdAt = this.createdAt,
    amount = this.amount,
    currency = this.currency,
    failureReason = this.failureReason,
    updatedAt = this.updatedAt,
)