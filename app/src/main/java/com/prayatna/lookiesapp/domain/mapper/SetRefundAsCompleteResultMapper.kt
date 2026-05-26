package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetRefundAsCompleteResponse
import com.prayatna.lookiesapp.domain.model.transaction.SetRefundAsCompleteResult

fun SetRefundAsCompleteResponse.toDomain(): SetRefundAsCompleteResult {
    return SetRefundAsCompleteResult(
        message = message,
        status = status
    )
}
