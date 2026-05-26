package com.prayatna.lookiesapp.presentation.admin.transactionList.state

import com.prayatna.lookiesapp.domain.model.admin.AdminTransaction

data class AdminTransactionListUiState(
    val isLoading: Boolean = false,
    val transactions: List<AdminTransaction> = emptyList(),
    val errorMessage: String? = null,
    val selectedStatus: String? = null,
    val limit: Int = 20,
    val offset: Int = 0,
    val isRefreshing: Boolean = false
)
