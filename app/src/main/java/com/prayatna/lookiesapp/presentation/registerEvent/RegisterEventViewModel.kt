package com.prayatna.lookiesapp.presentation.registerEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.domain.usecase.painting.GetArtistPaintingsUseCase
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterEventViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val getArtistPaintingsUseCase: GetArtistPaintingsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(RegisterEventUiState())
    val state: StateFlow<RegisterEventUiState> = _state.asStateFlow()

    init {
        fetchArtistPaintings()
    }

    fun onEvent(event: RegisterEventEvent) {
        when (event) {
            is RegisterEventEvent.SetEventId -> {
                _state.update { it.copy(eventId = event.id) }
            }
            is RegisterEventEvent.TogglePainting -> {
                _state.update { currentState ->
                    val newSelection = currentState.selectedIds.toMutableSet()

                    if (newSelection.contains(event.id)) {
                        newSelection.remove(event.id)
                    } else {
                        if (newSelection.size < currentState.maxLimit) {
                            newSelection.add(event.id)
                        }
                    }
                    currentState.copy(selectedIds = newSelection)
                }
            }

            is RegisterEventEvent.NextStep -> {
                _state.update { it.copy(currentStep = 2) }
            }

            is RegisterEventEvent.PrevStep -> {
                _state.update { it.copy(currentStep = 1) }
            }

            is RegisterEventEvent.Submit -> {
                submitRegistration()
            }

            is RegisterEventEvent.DismissError -> {
                _state.update { it.copy(errorMessage = null, isLoading = false) }
            }
        }
    }

    private fun fetchArtistPaintings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = getArtistPaintingsUseCase()

            _state.update { currentState ->
                when (result) {
                    is DataResult.Success -> {
                        currentState.copy(
                            isLoading = false,
                            allPaintings = result.data
                        )
                    }
                    is DataResult.Error -> {
                        currentState.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                    else -> {
                        currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun submitRegistration() {
        val currentState = _state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = artistRepository.registerEvent(
                eventId = currentState.eventId,
                paintingIds = currentState.selectedIds.toList()
            )

            _state.update {
                when (result) {
                    is DataResult.Success -> {
                        it.copy(isLoading = false, data = result.data, isSuccess = true, successMessage = result.data.message)
                    }
                    is DataResult.Error -> {
                        it.copy(isLoading = false, errorMessage = result.error)
                    }
                    else -> {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}