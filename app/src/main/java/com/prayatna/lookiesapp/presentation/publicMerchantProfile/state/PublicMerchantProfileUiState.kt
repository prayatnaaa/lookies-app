package com.prayatna.lookiesapp.presentation.publicMerchantProfile.state

import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness

data class PublicMerchantProfileUiState(
    val isLoading: Boolean = false,
    val merchant: MerchantBusiness? = null,
    val ongoingEvents: List<Event> = emptyList(),
    val errorMessage: String? = null,
    val businessId: String = ""
)
