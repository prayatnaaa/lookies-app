package com.prayatna.lookiesapp.data.remote.dto.request.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetRefundAsCompleteRequest(
    @SerialName("refund_request_id")
    val refundRequestId: String
)