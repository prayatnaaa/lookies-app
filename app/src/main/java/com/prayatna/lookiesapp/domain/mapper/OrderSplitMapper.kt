package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.OrderSplitDto
import com.prayatna.lookiesapp.data.remote.dto.PendingOrderSplitsDto
import com.prayatna.lookiesapp.domain.model.transaction.OrderSplit
import com.prayatna.lookiesapp.domain.model.transaction.PendingOrderSplits

fun OrderSplitDto.toDomain(): OrderSplit {
    return OrderSplit(
        id = id,
        orderId = orderId,
        merchantId = merchantId,
        grossAmount = grossAmount,
        platformFee = platformFee,
        netAmount = netAmount,
        payoutStatus = payoutStatus,
        payoutReference = payoutReference,
        createdAt = createdAt
    )
}

fun PendingOrderSplitsDto.toDomain(): PendingOrderSplits {
    return PendingOrderSplits(
        createdAt = createdAt,
        totalAmount = totalAmount
    )
}
