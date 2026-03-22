package com.prayatna.lookiesapp.domain.model.transaction


data class CreateQrisPaymentRequestInput (
    val merchantId: String,
    val orderId: String,
    val amount: Int,
    val description: String? = null
)