package com.prayatna.lookiesapp.presentation.payment.existingQrPayment.state

import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt

data class ExistingQrPaymentUiState(
    val isLoading: Boolean = false,
    val paymentAttempt: PaymentAttempt? = null,
    val errorMessage: String? = null,
    val isPaid: Boolean = false
)
