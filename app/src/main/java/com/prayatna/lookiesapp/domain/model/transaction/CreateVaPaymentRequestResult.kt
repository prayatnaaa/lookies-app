package com.prayatna.lookiesapp.domain.model.transaction

data class CreateVaPaymentRequestResult(
    val status: String,
    val message: String,
    val paymentRequestId: String? = null,
    val paymentMethodId: String? = null,
    val virtualAccountNumber: String? = null,
    val expiresAt: String? = null
)
