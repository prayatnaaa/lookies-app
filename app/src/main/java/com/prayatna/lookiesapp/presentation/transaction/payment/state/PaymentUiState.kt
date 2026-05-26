package com.prayatna.lookiesapp.presentation.transaction.payment.state

data class PaymentUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val paymentSuccessUrl: String? = null,

    val selectedMethod: PaymentMethod = PaymentMethod.GOPAY,

    val phoneNumber: String = "",
    val cardNumber: String = "",
    val cardExpiry: String = "",
    val cardCvv: String = ""
)

enum class PaymentMethod(val title: String, val code: String) {
    GOPAY("GoPay", "GOPAY_RECURRING"),
    CREDIT_CARD("Credit / Debit Card", "CARDS")
}