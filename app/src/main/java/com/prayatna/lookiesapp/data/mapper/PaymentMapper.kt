package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.PaymentDto
import com.prayatna.lookiesapp.domain.model.payment.Payment

fun PaymentDto.asDomainModel(): Payment {
    return Payment(
        id = this.id,
        userId = this.userId,
        amount = this.amount,
        status = this.status,
        paymentType = this.paymentType,
        quantity = this.quantity
    )
}