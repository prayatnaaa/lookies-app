package com.prayatna.lookiesapp.presentation.admin.transactionDetail.state

sealed interface AdminTransactionDetailUiEvent {
    data class LoadTransactionDetail(val orderId: String) : AdminTransactionDetailUiEvent
    data object OnBackClicked : AdminTransactionDetailUiEvent
    data object Refresh : AdminTransactionDetailUiEvent
}
