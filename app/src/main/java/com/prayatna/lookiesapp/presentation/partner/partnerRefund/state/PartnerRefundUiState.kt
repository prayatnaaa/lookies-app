package com.prayatna.lookiesapp.presentation.partner.partnerRefund.state

import com.prayatna.lookiesapp.domain.model.transaction.Refund

data class PartnerRefundUiState (
    val error: String? = null,
    val isLoading: Boolean = false,
    val data: Refund? = null,
    val notes: String? = null,
    val updatedData: Refund? = null,
    val status: String = "pending"
)