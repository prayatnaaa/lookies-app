package com.prayatna.lookiesapp.presentation.payment.existingVaPayment.state

import com.prayatna.lookiesapp.domain.model.payment.PaymentAttempt

data class ExistingVaPaymentUiState(
    val isLoading: Boolean = false,
    val paymentAttempt: PaymentAttempt? = null,
    val errorMessage: String? = null,
    val isPaid: Boolean = false
)
