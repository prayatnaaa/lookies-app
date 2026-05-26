package com.prayatna.lookiesapp.domain.model.payment

data class AddPaymentResult(
    val message: String,
    val data: Payment? = null
)
