package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.model.shipment.ExhibitionShipment
import com.prayatna.lookiesapp.domain.model.user.BusinessAddress

data class UnsoldArtworkReturnUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Context
    val eventPainting: EventPainting? = null,
    val returnAddress: BusinessAddress? = null,
    val shipment: ExhibitionShipment? = null,

    // Form
    val courierNameInput: String = "",
    val trackingNumberInput: String = "",
    val notesInput: String = "",
    
    // Display info
    val paintingTitle: String = "",
    val artistName: String = "",
    val eventTitle: String = ""
)
