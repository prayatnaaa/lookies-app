package com.prayatna.lookiesapp.presentation.shipment.detail.state

import com.prayatna.lookiesapp.domain.model.transaction.Shipment

data class ShipmentDetailUiState(
    val isLoading: Boolean = false,
    val shipment: Shipment? = null,
    val trackingNumberInput: String? = "",
    val selectedStatus: String = "",
    val errorMessage: String? = null,
    val isUpdating: Boolean = false,
    val updateSuccessMessage: String? = null,
    val availableStatuses: List<String> = listOf("processing", "shipped", "delivered", "pending", "cancelled", "refunded")
)
