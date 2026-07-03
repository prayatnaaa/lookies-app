package com.prayatna.lookiesapp.presentation.admin.transactionDetail.state

sealed interface AdminTransactionDetailUiEffect {
    data object NavigateBack : AdminTransactionDetailUiEffect
    data class ShowError(val message: String) : AdminTransactionDetailUiEffect
}
