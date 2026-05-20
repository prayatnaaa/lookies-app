package com.prayatna.lookiesapp.presentation.shipmentDetail.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.transaction.Transaction

data class ShipmentDetailUiState(
    val isLoading: Boolean = false,
    val shipment: Shipment? = null,
    val transactionDetail: Transaction? = null,
    val trackingNumberInput: String? = "",
    val selectedStatus: String = "",
    val errorMessage: String? = null,
    val isUpdating: Boolean = false,
    val isUploadingProof: Boolean = false,
    val selectedArrivalProof: Uri? = null,
    val updateSuccessMessage: String? = null,
    val availableStatuses: List<String> = listOf("processing", "shipped", "delivered", "pending", "cancelled", "refunded")
)
