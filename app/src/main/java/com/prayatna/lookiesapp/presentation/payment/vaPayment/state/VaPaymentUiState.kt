package com.prayatna.lookiesapp.presentation.payment.vaPayment.state

import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestResult

data class VaPaymentUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val vaPaymentData: CreateVaPaymentRequestResult? = null,
    val isPaid: Boolean = false
)
