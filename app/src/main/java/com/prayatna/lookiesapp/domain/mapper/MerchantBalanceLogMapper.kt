package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.MerchantBalanceLogDto
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog

fun MerchantBalanceLogDto.toDomain(): MerchantBalanceLog {
    return MerchantBalanceLog(
        id = id,
        merchantId = merchantId,
        transactionType = transactionType,
        amount = amount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        refId = refId,
        orderId = orderId,
        eventId = eventId,
        description = description,
        createdAt = createdAt
    )
}
