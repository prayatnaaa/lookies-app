package com.prayatna.lookiesapp.presentation.refund.refundDetail.state

import com.prayatna.lookiesapp.domain.model.transaction.Refund

data class RefundDetailUiState(
    val isLoading: Boolean = false,
    val refund: Refund? = null,
    val errorMessage: String? = null,
    val trackingNumber: String = "",
    val isSubmittingTracking: Boolean = false
)
