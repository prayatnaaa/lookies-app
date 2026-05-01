package com.prayatna.lookiesapp.presentation.partner.main.home.state

import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.partner.PartnerDashboard
import com.prayatna.lookiesapp.domain.model.transaction.MonthlyFinancialReport

data class PartnerHomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val profile: MerchantProfile? = null,
    val dashboard: PartnerDashboard? = null,
    val monthlyFinancialReport: List<MonthlyFinancialReport> = emptyList(),
    val errorMessage: String? = null,
    val filterStartDate: String? = null,
    val filterEndDate: String? = null,
    val filterItemType: String? = null,
    val filterEventId: Int? = null
)