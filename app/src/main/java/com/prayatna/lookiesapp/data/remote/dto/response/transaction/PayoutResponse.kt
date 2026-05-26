package com.prayatna.lookiesapp.data.remote.dto.response.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayoutResponse(
    val status: String,
    val message: String,
    val data: PayoutDataDto? = null
)

@Serializable
data class PayoutDataDto(
    @SerialName("withdrawal_id")
    val withdrawalId: String,
    @SerialName("payout_id")
    val payoutId: String,
    @SerialName("xendit_status")
    val xenditStatus: String
)
