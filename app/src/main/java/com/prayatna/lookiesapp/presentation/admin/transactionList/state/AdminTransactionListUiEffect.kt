package com.prayatna.lookiesapp.presentation.admin.transactionList.state

sealed interface AdminTransactionListUiEffect {
    data object NavigateBack : AdminTransactionListUiEffect
    data class NavigateToDetail(val orderId: String) : AdminTransactionListUiEffect
    data class ShowError(val message: String) : AdminTransactionListUiEffect
}
