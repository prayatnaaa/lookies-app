package com.prayatna.lookiesapp.presentation.partner.main.home.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard

data class PartnerHomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val profile: MerchantProfile? = null,
    val dashboard: PartnerDashboard? = null,
    val errorMessage: String? = null
)