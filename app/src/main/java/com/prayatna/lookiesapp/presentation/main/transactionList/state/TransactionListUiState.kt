package com.prayatna.lookiesapp.presentation.main.transactionList.state

import com.prayatna.lookiesapp.domain.model.transaction.Transaction

sealed class TransactionListUiState {
    data object Loading : TransactionListUiState()
    data object Empty : TransactionListUiState()
    data class Success(val data: List<Transaction>, val customerName: String = "Customer", val isRefreshing: Boolean = false) : TransactionListUiState()
    data class Error(val message: String) : TransactionListUiState()
}