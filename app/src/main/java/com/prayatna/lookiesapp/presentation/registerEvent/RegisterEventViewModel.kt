package com.prayatna.lookiesapp.presentation.registerEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.domain.usecase.event.GetRevenueRulesByEventIdUseCase
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
    private val getArtistPaintingsUseCase: GetArtistPaintingsUseCase,
    private val getRevenueRulesByEventIdUseCase: GetRevenueRulesByEventIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterEventUiState())
    val state: StateFlow<RegisterEventUiState> = _state.asStateFlow()

    init {
        fetchArtistPaintings()
    }

    fun onEvent(event: RegisterEventEvent) {
        when (event) {
            is RegisterEventEvent.SetEventId -> {
                _state.update { it.copy(eventId = event.id) }
                fetchRevenueRules(event.id)
            }
            is RegisterEventEvent.SetFee -> _state.update { it.copy(fee = event.fee) }
            is RegisterEventEvent.SetMerchantId -> _state.update { it.copy(merchantId = event.merchantId) }
            is RegisterEventEvent.SetMaxLimit -> _state.update { it.copy(maxLimit = event.maxLimit) }
            
            RegisterEventEvent.NextStep -> {
                if (_state.value.currentStep < 2) {
                    _state.update { it.copy(currentStep = it.currentStep + 1) }
                }
            }
            RegisterEventEvent.PrevStep -> {
                if (_state.value.currentStep > 0) {
                    _state.update { it.copy(currentStep = it.currentStep - 1) }
                }
            }
            is RegisterEventEvent.TogglePainting -> {
                val currentSelected = _state.value.selectedIds.toMutableSet()
                if (currentSelected.contains(event.id)) {
                    currentSelected.remove(event.id)
                } else {
                    currentSelected.add(event.id)
                }
                _state.update { it.copy(selectedIds = currentSelected) }
            }
            RegisterEventEvent.Submit -> submitRegistration()
            RegisterEventEvent.DismissError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun fetchRevenueRules(eventId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isRevenueLoading = true) }
            when (val result = getRevenueRulesByEventIdUseCase(eventId)) {
                is DataResult.Success -> {
                    _state.update { it.copy(
                        revenueRules = result.data,
                        isRevenueLoading = false
                    ) }
                }
                is DataResult.Error -> {
                    _state.update { it.copy(
                        errorMessage = result.error,
                        isRevenueLoading = false
                    ) }
                }
                else -> _state.update { it.copy(isRevenueLoading = false) }
            }
        }
    }

    private fun fetchArtistPaintings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getArtistPaintingsUseCase()) {
                is DataResult.Success -> {
                    _state.update { it.copy(
                        allPaintings = result.data,
                        isLoading = false
                    ) }
                }
                is DataResult.Error -> {
                    _state.update { it.copy(
                        errorMessage = result.error,
                        isLoading = false
                    ) }
                }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitRegistration() {
        val currentState = _state.value
        if (currentState.selectedIds.isEmpty()) {
            _state.update { it.copy(errorMessage = "Please select at least one painting") }
            return
        }
        
        if (currentState.selectedIds.size > currentState.maxLimit) {
            _state.update { it.copy(errorMessage = "You can only select up to ${currentState.maxLimit} paintings") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = artistRepository.registerEvent(
                eventId = currentState.eventId,
                paintingIds = currentState.selectedIds.toList()
            )) {
                is DataResult.Success -> {
                    _state.update { it.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Registration submitted successfully",
                        data = result.data
                    ) }
                }
                is DataResult.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = result.error
                    ) }
                }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
