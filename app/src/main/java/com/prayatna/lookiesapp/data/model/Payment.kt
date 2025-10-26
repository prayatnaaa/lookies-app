package com.prayatna.lookiesapp.data.model

data class Payment(
    val id: Int?,
    val userId: Int,
    val amount: Double,
    val status: String,
    val paymentType: String,
    val quantity: Int = 1
)
