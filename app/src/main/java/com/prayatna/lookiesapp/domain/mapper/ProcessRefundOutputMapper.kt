package com.prayatna.lookiesapp.domain.mapper

import com.prayatna.lookiesapp.data.remote.dto.response.refund.ProcessRefundResponse
import com.prayatna.lookiesapp.domain.model.refund.ProcessRefundOutput

fun ProcessRefundResponse.toDomain() =
    ProcessRefundOutput(
        message = message,
        status = status
    )