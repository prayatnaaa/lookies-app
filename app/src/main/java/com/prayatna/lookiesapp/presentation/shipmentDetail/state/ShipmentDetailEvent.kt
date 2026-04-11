package com.prayatna.lookiesapp.presentation.shipmentDetail.state

sealed class ShipmentDetailEvent {
    data class OnStatusSelected(val status: String) : ShipmentDetailEvent()
    data class OnTrackingNumberChanged(val trackingNumber: String) : ShipmentDetailEvent()
    data object SubmitStatusUpdate : ShipmentDetailEvent()
    data object SubmitTrackingNumberUpdate : ShipmentDetailEvent()
    data object OnErrorConfirmed : ShipmentDetailEvent()
    data object OnSuccessConfirmed : ShipmentDetailEvent()
    data object OnBackClick : ShipmentDetailEvent()
}
