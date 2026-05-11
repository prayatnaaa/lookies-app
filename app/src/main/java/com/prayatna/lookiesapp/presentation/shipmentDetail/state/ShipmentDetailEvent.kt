package com.prayatna.lookiesapp.presentation.shipmentDetail.state

import android.net.Uri

sealed class ShipmentDetailEvent {
    data class OnStatusSelected(val status: String) : ShipmentDetailEvent()
    data class OnTrackingNumberChanged(val trackingNumber: String) : ShipmentDetailEvent()
    data object SubmitStatusUpdate : ShipmentDetailEvent()
    data object SubmitTrackingNumberUpdate : ShipmentDetailEvent()
    data class OnArrivalProofSelected(val image: Uri) : ShipmentDetailEvent()
    data object SubmitArrivalProof : ShipmentDetailEvent()
    data object OnErrorConfirmed : ShipmentDetailEvent()
    data object OnSuccessConfirmed : ShipmentDetailEvent()
    data object OnBackClick : ShipmentDetailEvent()
}
