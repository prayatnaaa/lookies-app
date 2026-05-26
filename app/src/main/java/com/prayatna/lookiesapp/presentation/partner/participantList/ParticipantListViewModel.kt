package com.prayatna.lookiesapp.presentation.partner.participantList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.partner.GetParticipantUseCase
import com.prayatna.lookiesapp.presentation.partner.participantList.state.ParticipantListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantListViewModel @Inject constructor(
    private val getParticipantUseCase: GetParticipantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParticipantListUiState())
    val uiState: StateFlow<ParticipantListUiState> = _uiState

    fun getParticipants(eventId: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = getParticipantUseCase(eventId)) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            participants = result.data
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}
