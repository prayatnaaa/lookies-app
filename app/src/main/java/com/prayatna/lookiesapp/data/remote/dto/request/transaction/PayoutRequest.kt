package com.prayatna.lookiesapp.data.remote.dto.request.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayoutRequest(
    @SerialName("withdrawal_id")
    val withdrawalId: String
)
