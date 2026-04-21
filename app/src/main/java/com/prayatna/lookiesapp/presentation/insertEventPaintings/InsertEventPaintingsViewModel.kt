package com.prayatna.lookiesapp.presentation.insertEventPaintings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.Painting
import com.prayatna.lookiesapp.domain.usecase.painting.GetArtistPaintingsUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.InsertSelfPaintingsUseCase
import com.prayatna.lookiesapp.presentation.insertEventPaintings.state.InsertEventPaintingsUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertEventPaintingsViewModel @Inject constructor(
    private val insertSelfPaintingsUseCase: InsertSelfPaintingsUseCase,
    private val getArtistPaintingUseCase: GetArtistPaintingsUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(InsertEventPaintingsUiState())
    val uiState: StateFlow<InsertEventPaintingsUiState> = _uiState.asStateFlow()

    init {
        val merchantId = checkNotNull(savedStateHandle.get<String>("merchantId"))
        getSelfEventPaintings(merchantId)
    }

    private fun getSelfEventPaintings(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getArtistPaintingUseCase(id = id)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.error,
                        isSuccess = false
                    ) }
                }
                DataResult.Loading -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = null,
                    ) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = null,
                        availablePaintings = result.data
                    ) }
                }
                else -> Unit
            }
        }
    }

    fun togglePainting(painting: Painting) {
        _uiState.update { currentState ->
            val newSelection = currentState.selectedPaintings.toMutableSet()
            if (newSelection.contains(painting)) {
                newSelection.remove(painting)
            } else {
                newSelection.add(painting)
            }
            currentState.copy(selectedPaintings = newSelection)
        }
    }

    fun submitSelectedPaintings() {
        val eventId = checkNotNull(savedStateHandle.get<Int>("eventId"))
        if (_uiState.value.selectedPaintings.isEmpty()) return
        viewModelScope.launch {
            when (val result = insertSelfPaintingsUseCase(
                eventId = eventId,
                selectedPaintings = _uiState.value.selectedPaintings.toList()
            )) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        isSubmitting = false,
                        error = result.error,
                        isSuccess = false
                    ) }
                }
                is DataResult.Success -> {
                    _uiState.update { it.copy(
                        isSubmitting = false,
                        error = null,
                        isSuccess = true
                    ) }
                }
                else -> Unit
            }
        }
    }
}