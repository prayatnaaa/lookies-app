package com.prayatna.lookiesapp.presentation.partner.eventTransactions.state

import com.prayatna.lookiesapp.domain.model.transaction.PaidOrderItem

data class EventTransactionListUiState(
    val isLoading: Boolean = false,
    val items: List<PaidOrderItem> = emptyList(),
    val errorMessage: String? = null,
    val eventId: Int = 0
)
