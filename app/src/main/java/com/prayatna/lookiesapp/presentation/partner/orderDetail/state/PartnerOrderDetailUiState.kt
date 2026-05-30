package com.prayatna.lookiesapp.presentation.partner.orderDetail.state

import com.prayatna.lookiesapp.domain.model.transaction.Transaction

data class PartnerOrderDetailUiState(
    val isLoading: Boolean = false,
    val order: Transaction? = null,
    val errorMessage: String? = null
)
