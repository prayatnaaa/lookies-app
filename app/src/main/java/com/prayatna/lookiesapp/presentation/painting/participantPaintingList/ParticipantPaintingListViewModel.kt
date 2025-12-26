package com.prayatna.lookiesapp.presentation.painting.participantPaintingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingsUseCase
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state.ParticipantPaintingListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantPaintingListViewModel @Inject constructor(
    private val getEventPaintingsUseCase: GetEventPaintingsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ParticipantPaintingListUiState())
    val uiState: StateFlow<ParticipantPaintingListUiState> = _uiState.asStateFlow()

    fun loadPaintings(participantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getEventPaintingsUseCase(participantId)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error,
                            isLoading = false
                        )
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            eventPaintings = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}