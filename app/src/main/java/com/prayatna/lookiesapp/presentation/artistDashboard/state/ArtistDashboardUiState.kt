package com.prayatna.lookiesapp.presentation.artistDashboard.state

import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardData

sealed class ArtistDashboardUiState {
    data object Loading : ArtistDashboardUiState()
    data class Success(val data: ArtistDashboardData) : ArtistDashboardUiState()
    data class Error(val message: String) : ArtistDashboardUiState()
}