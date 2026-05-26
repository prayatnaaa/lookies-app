package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.transaction.PayoutResponse
import com.prayatna.lookiesapp.domain.model.transaction.PayoutResult

fun PayoutResponse.toDomain(): PayoutResult {
    return PayoutResult(
        status = status,
        message = message,
        withdrawalId = data?.withdrawalId,
        payoutId = data?.payoutId,
        xenditStatus = data?.xenditStatus
    )
}
