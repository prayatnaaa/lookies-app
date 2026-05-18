package com.prayatna.lookiesapp.presentation.artistDashboard.state

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.domain.model.transaction.PendingOrderSplits

data class ArtistDashboardUiState(
    val isLoading: Boolean = false,
    val summary: ArtistDashboardSummary? = null,
    val profile: MerchantProfile? = null,
    val pendingOrderSplits: PendingOrderSplits? = null,
    val balanceLogs: List<MerchantBalanceLog> = emptyList(),
    val errorMessage: String? = null
)
