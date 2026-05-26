package com.prayatna.lookiesapp.presentation.admin.transactionList.state

sealed interface AdminTransactionListUiEvent {
    data object LoadTransactions : AdminTransactionListUiEvent
    data object RefreshTransactions : AdminTransactionListUiEvent
    data class OnStatusFilterChanged(val status: String?) : AdminTransactionListUiEvent
    data class OnTransactionClicked(val orderId: String) : AdminTransactionListUiEvent
    data object OnBackClicked : AdminTransactionListUiEvent
}
