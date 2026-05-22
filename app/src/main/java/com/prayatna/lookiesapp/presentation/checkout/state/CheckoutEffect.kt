package com.prayatna.lookiesapp.presentation.checkout.state

sealed class CheckoutEffect {

    data class ShowSnackbar(val message: String) : CheckoutEffect()

    data class ShowErrorDialog(val message: String) : CheckoutEffect()
    data object ShowSuccessDialog : CheckoutEffect()

    data object NavigateBack : CheckoutEffect()
    data object NavigateToAddAddress : CheckoutEffect()

    data class NavigateToQrisPayment(
        val orderId: String,
        val merchantId: String,
        val amount: Long
    ) : CheckoutEffect()

    data class NavigateToVaPayment(
        val orderId: String,
        val merchantId: String,
        val amount: Long,
        val bankCode: String,
        val customerName: String
    ) : CheckoutEffect()
}