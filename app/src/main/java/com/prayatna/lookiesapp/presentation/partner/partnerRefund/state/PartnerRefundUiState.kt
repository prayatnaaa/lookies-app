package com.prayatna.lookiesapp.presentation.partner.partnerRefund.state

import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund

data class PartnerRefundUiState (
    val error: String? = null,
    val isLoading: Boolean = false,
    val data: DetailRefund? = null,
    val notes: String? = null,
    val status: String = "pending"
)