package com.prayatna.lookiesapp.presentation.partner.main.home.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard

sealed class PartnerHomeUiState {
    data object Loading : PartnerHomeUiState()
    data class Success(
        val profile: MerchantProfile,
        val dashboard: PartnerDashboard? = null
    ) : PartnerHomeUiState()
    data class Error(val message: String) : PartnerHomeUiState()
}