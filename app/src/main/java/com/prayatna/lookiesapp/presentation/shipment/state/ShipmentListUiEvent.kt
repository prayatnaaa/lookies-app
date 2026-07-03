package com.prayatna.lookiesapp.presentation.shipment.state

sealed interface ShipmentListUiEvent {
    data class LoadShipments(val merchantId: String) : ShipmentListUiEvent
    data class FilterByStatus(val status: String?) : ShipmentListUiEvent
    data class SearchQueryChanged(val query: String) : ShipmentListUiEvent
    data object Refresh : ShipmentListUiEvent
}
