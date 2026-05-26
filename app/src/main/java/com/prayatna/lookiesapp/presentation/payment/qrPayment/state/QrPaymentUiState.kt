package com.prayatna.lookiesapp.presentation.payment.qrPayment.state

import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestResult

data class QrPaymentUiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val qrPaymentData: CreateQrisPaymentRequestResult? = null,
    val isPaid: Boolean = false
)