package com.prayatna.lookiesapp.domain.model.transaction

enum class PaymentRequestMethod(val title: String, val code: String) {
    GOPAY("GoPay", "GOPAY_RECURRING"),
    CREDIT_CARD("Credit / Debit Card", "CARDS")
}

data class CreatePaymentParams(
    val selectedMethod: PaymentRequestMethod = PaymentRequestMethod.GOPAY,
    val phoneNumber: String = "",
    val cardNumber: String = "",
    val cardExpiry: String = "",
    val cardCvv: String = ""
)