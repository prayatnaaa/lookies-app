package com.prayatna.lookiesapp.presentation.artistDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.presentation.artistDashboard.state.ArtistDashboardUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistDashboardViewModel @Inject constructor(
    repository: ArtistRepository
) : ViewModel() {

    val state: StateFlow<ArtistDashboardUiState> = repository.getDashboardData()
        .map { result ->
            when (result) {
                is DataResult.Success -> ArtistDashboardUiState.Success(result.data)
                is DataResult.Error -> ArtistDashboardUiState.Error(result.error)
                else -> ArtistDashboardUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArtistDashboardUiState.Loading
        )
}