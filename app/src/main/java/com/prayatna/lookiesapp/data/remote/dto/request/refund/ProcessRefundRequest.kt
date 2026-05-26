package com.prayatna.lookiesapp.data.remote.dto.request.refund

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessRefundRequest(
    @SerialName("refund_request_id")
    val refundRequestId: String
)