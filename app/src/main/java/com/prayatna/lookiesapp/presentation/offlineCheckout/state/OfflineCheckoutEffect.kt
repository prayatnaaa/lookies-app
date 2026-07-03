package com.prayatna.lookiesapp.presentation.offlineCheckout.state

sealed interface OfflineCheckoutEffect {
    data object NavigateBack : OfflineCheckoutEffect
    data class ShowError(val message: String) : OfflineCheckoutEffect
    data class NavigateToSuccess(val orderId: String) : OfflineCheckoutEffect

    data class NavigateToQrisPayment(
        val orderId: String,
        val merchantId: String,
        val amount: Long
    ) : OfflineCheckoutEffect

    data class NavigateToVaPayment(
        val orderId: String,
        val merchantId: String,
        val amount: Long,
        val bankCode: String,
        val customerName: String
    ) : OfflineCheckoutEffect
}
