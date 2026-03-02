package com.prayatna.lookiesapp.presentation.artistDashboard.state

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary


sealed class ArtistDashboardUiState {
    data object Loading : ArtistDashboardUiState()
    data class Success(val data: ArtistDashboardSummary) : ArtistDashboardUiState()
    data class Error(val message: String) : ArtistDashboardUiState()
}