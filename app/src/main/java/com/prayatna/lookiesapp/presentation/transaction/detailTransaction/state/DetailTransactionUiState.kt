package com.prayatna.lookiesapp.presentation.transaction.detailTransaction.state

import com.prayatna.lookiesapp.domain.model.transaction.DetailTransaction

data class DetailTransactionUiState(
    val data: DetailTransaction? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)