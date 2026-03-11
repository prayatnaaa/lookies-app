package com.prayatna.lookiesapp.domain.model.transaction

import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod

data class CreatePaymentParams(
    val selectedMethod: PaymentMethod = PaymentMethod.GOPAY,
    val phoneNumber: String = "",
    val cardNumber: String = "",
    val cardExpiry: String = "",
    val cardCvv: String = ""
)