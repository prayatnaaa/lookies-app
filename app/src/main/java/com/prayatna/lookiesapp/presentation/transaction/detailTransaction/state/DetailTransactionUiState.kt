package com.prayatna.lookiesapp.presentation.transaction.detailTransaction.state

import com.prayatna.lookiesapp.domain.model.transaction.DetailTransaction
import com.prayatna.lookiesapp.domain.model.shipment.Shipment

data class DetailTransactionUiState(
    val data: DetailTransaction? = null,
    val shipment: Shipment? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isCompleting: Boolean = false
)