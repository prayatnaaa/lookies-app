package com.prayatna.lookiesapp.presentation.payment.addpayment.state

import com.prayatna.lookiesapp.domain.model.payment.AddPaymentResult

data class AddPaymentUiState(
    val isLoading: Boolean = false,
    val success: AddPaymentResult? = null,
    val error: String? = null
)
