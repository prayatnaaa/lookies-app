package com.prayatna.lookiesapp.presentation.transaction.payment.state

sealed class PaymentEvent {
    data class SelectMethod(val method: PaymentMethod) : PaymentEvent()
    data class PhoneChanged(val value: String) : PaymentEvent()
    data class CardNumberChanged(val value: String) : PaymentEvent()
    data class CardExpiryChanged(val value: String) : PaymentEvent()
    data class CardCvvChanged(val value: String) : PaymentEvent()

    data class SubmitPayment(
        val orderId: String,
        val merchantId: String,
        val amount: Double
    ) : PaymentEvent()
}