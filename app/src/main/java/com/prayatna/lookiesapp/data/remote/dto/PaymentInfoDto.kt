package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentInfoDto(
    @SerialName("payment_id")
    val paymentId: String? = null,

    @SerialName("invoice_id")
    val invoiceId: String? = null,

    val status: String? = null,

    val provider: String? = null
)