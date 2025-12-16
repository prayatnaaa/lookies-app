package com.prayatna.lookiesapp.data.remote.dto.response.payment

import com.prayatna.lookiesapp.data.remote.dto.PaymentDto
import kotlinx.serialization.Serializable

@Serializable
data class AddPaymentResponse(
    val message: String,
    val data: PaymentDto? = null
)
