package com.prayatna.lookiesapp.presentation.user.partnerSubmission.state

sealed interface PartnerSubmissionUiState {
    data object Idle : PartnerSubmissionUiState
    data object Loading : PartnerSubmissionUiState
    data class Success(val businessId: String) : PartnerSubmissionUiState
    data class Error(val message: String) : PartnerSubmissionUiState
}