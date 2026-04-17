package com.prayatna.lookiesapp.presentation.artistDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.artist.ArtistDashboardSummary
import com.prayatna.lookiesapp.domain.usecase.artist.GetArtistDashboardSummaryUseCase
import com.prayatna.lookiesapp.presentation.artistDashboard.state.ArtistDashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistDashboardViewModel @Inject constructor(
    getDashboardSummaryUseCase: GetArtistDashboardSummaryUseCase
) : ViewModel() {

    val state: StateFlow<ArtistDashboardUiState> = getDashboardSummaryUseCase()
        .map<ArtistDashboardSummary, ArtistDashboardUiState> { data -> 
            ArtistDashboardUiState.Success(data) 
        }
        .onStart { emit(ArtistDashboardUiState.Loading) }
        .catch { e -> emit(ArtistDashboardUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArtistDashboardUiState.Loading
        )
}