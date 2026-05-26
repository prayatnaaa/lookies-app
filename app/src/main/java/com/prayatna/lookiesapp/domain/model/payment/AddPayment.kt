package com.prayatna.lookiesapp.domain.model.payment

data class AddPayment (
    val userId: String?,
    val amount: Double,
    val paymentType: String,
    val quantity: Int
)