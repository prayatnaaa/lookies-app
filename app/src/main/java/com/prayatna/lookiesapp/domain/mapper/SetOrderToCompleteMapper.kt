package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteResponse
import com.prayatna.lookiesapp.domain.model.payment.SetOrderToCompleteResult

fun SetOrderToCompleteResponse.toDomain() = SetOrderToCompleteResult(
    message = message,
    status = status
)