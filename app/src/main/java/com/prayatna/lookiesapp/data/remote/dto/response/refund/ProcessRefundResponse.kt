package com.prayatna.lookiesapp.data.remote.dto.response.refund

import kotlinx.serialization.Serializable

@Serializable
data class ProcessRefundResponse(
    val message: String,
    val status: String
)