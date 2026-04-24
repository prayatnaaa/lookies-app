package com.prayatna.lookiesapp.presentation.shipment.state

import com.prayatna.lookiesapp.domain.model.shipment.Shipment

data class ShipmentListUiState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val data: List<Shipment> = emptyList()
)