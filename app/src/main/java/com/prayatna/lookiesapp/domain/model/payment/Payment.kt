package com.prayatna.lookiesapp.domain.model.payment

data class Payment(
    val id: Int?,
    val userId: Int,
    val amount: Double,
    val status: String,
    val paymentType: String,
    val quantity: Int = 1
)
