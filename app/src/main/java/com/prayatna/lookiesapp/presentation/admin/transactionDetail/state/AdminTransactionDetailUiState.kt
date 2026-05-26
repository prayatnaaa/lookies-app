package com.prayatna.lookiesapp.presentation.admin.transactionDetail.state

import com.prayatna.lookiesapp.domain.model.admin.AdminTransactionDetail

import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.transaction.DetailTransaction

data class AdminTransactionDetailUiState(
    val isLoading: Boolean = false,
    val transaction: AdminTransactionDetail? = null,
    val detailTransaction: DetailTransaction? = null,
    val shipment: Shipment? = null,
    val errorMessage: String? = null
)
