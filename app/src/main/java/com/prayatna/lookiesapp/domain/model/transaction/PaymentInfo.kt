package com.prayatna.lookiesapp.domain.model.transaction

data class PaymentInfo(
    val paymentId: String? = null,
    val invoiceId: String,
    val status: String,
    val provider: String
)