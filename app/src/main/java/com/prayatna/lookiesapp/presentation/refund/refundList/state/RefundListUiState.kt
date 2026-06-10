package com.prayatna.lookiesapp.presentation.refund.refundList.state

import com.prayatna.lookiesapp.domain.model.transaction.Refund

data class RefundListUiState(
    val isLoading: Boolean = false,
    val refunds: List<Refund> = emptyList(),
    val errorMessage: String? = null,
    val isProcessing: Boolean = false,
    val successMessage: String? = null,
    val selectedStatus: String? = null
)
