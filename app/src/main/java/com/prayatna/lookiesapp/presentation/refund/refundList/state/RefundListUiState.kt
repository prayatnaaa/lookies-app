package com.prayatna.lookiesapp.presentation.refund.refundList.state

data class RefundListUiState(
    val isLoading: Boolean = false,
    val refunds: List<com.prayatna.lookiesapp.domain.model.transaction.Refund> = emptyList(),
    val errorMessage: String? = null,
    val isProcessing: Boolean = false,
    val successMessage: String? = null
)
