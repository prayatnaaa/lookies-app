package com.prayatna.lookiesapp.data.remote.dto.response.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CreateVaPaymentResponse(
    val status: String,
    val message: String,
    val data: VaPaymentDataDto? = null
)

@Serializable
data class VaPaymentDataDto(
    @SerialName("payment_request_id")
    val paymentRequestId: String,
    @SerialName("payment_method_id")
    val paymentMethodId: String,
    @SerialName("virtual_account_number")
    val virtualAccountNumber: String,
    @SerialName("expires_at")
    val expiresAt: String,
    val raw: JsonObject? = null
)
