package com.prayatna.lookiesapp.presentation.exhibitionShipment.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.shipment.DeliveryMethod

sealed class ExhibitionShipmentEvent {

    data class OnDeliveryMethodSelected(val method: DeliveryMethod) : ExhibitionShipmentEvent()

    data class OnCourierNameChanged(val name: String) : ExhibitionShipmentEvent()

    data class OnTrackingNumberChanged(val trackingNumber: String) : ExhibitionShipmentEvent()

    data object SubmitInboundShipment : ExhibitionShipmentEvent()

    data class OnGalleryNotesChanged(val notes: String) : ExhibitionShipmentEvent()
    data class OnArrivalProofSelected(val image: Uri) : ExhibitionShipmentEvent()
    data object SubmitArrivalProof : ExhibitionShipmentEvent()
    data object ConfirmArtworkReceived : ExhibitionShipmentEvent()

    data class OnReturnTrackingNumberChanged(val trackingNumber: String) : ExhibitionShipmentEvent()

    data object SubmitReturnShipment : ExhibitionShipmentEvent()

    data class OnArtistConditionNotesChanged(val notes: String) : ExhibitionShipmentEvent()
    data object ConfirmArtworkReturned : ExhibitionShipmentEvent()

    data object DismissError : ExhibitionShipmentEvent()
    data object DismissSuccess : ExhibitionShipmentEvent()
    data object OnBackClick : ExhibitionShipmentEvent()
}
