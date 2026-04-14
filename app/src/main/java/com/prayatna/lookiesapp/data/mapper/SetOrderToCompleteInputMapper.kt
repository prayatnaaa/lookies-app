package com.prayatna.lookiesapp.data.mapper

import com.prayatna.lookiesapp.data.remote.dto.request.payment.SetOrderToCompleteRequest
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteInput

fun SetOrderToCompleteInput.toDto() = SetOrderToCompleteRequest(
    orderId = orderId
)