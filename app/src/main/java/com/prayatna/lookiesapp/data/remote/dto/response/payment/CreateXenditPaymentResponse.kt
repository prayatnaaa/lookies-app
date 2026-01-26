package com.prayatna.lookiesapp.data.remote.dto.response.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CreateXenditPaymentResponse(
    val status: String,
    val message: String,
    @SerialName("payment_attempt_id") val paymentAttemptId: String? = null,
    @SerialName("payment_token") val paymentToken: JsonObject? = null
)