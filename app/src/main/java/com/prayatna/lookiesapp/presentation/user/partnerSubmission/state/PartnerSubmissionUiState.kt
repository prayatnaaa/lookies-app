package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state

import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel

sealed class PartnerSubmissionUiState {
    data object Idle : PartnerSubmissionUiState()
    data object Loading : PartnerSubmissionUiState()
    data class Success(val businessId: RoleApplicationResponse) : PartnerSubmissionUiState()
    data class Error(val message: String) : PartnerSubmissionUiState()

    data class MetaLoaded(val payoutChannels: List<PayoutChannel>) : PartnerSubmissionUiState()
}
