package com.prayatna.lookiesapp.presentation.user.artistSubmission.state

import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel

sealed class ArtistSubmissionUiState {
    data object Idle : ArtistSubmissionUiState()
    data object Loading : ArtistSubmissionUiState()
    data class Success(val response: RoleApplicationResponse) : ArtistSubmissionUiState()
    data class Error(val message: String) : ArtistSubmissionUiState()

    data class MetaLoaded(val payoutChannels: List<PayoutChannel>) : ArtistSubmissionUiState()
}
