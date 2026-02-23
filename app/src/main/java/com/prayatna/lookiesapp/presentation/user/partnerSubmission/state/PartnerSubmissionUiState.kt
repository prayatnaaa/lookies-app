package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state

import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse

sealed interface PartnerSubmissionUiState {
    data object Idle : PartnerSubmissionUiState
    data object Loading : PartnerSubmissionUiState
    data class Success(val businessId: RoleApplicationResponse) : PartnerSubmissionUiState
    data class Error(val message: String) : PartnerSubmissionUiState
}