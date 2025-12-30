package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.transaction.CheckoutResponse
import com.prayatna.lookiesapp.domain.model.transaction.CheckoutOutput

fun CheckoutResponse.toDomain(): CheckoutOutput {
    return CheckoutOutput(
        orderId = this.orderId,
        transactionId = this.transactionId,
        status = this.status
    )
}