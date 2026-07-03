package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.payment.GetPayoutChannelsUseCase
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state.SelectPayoutChannelUiEvent
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state.SelectPayoutChannelUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPayoutChannelViewModel @Inject constructor(
    private val getPayoutChannelsUseCase: GetPayoutChannelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectPayoutChannelUiState())
    val uiState: StateFlow<SelectPayoutChannelUiState> = _uiState.asStateFlow()

    init {
        onEvent(SelectPayoutChannelUiEvent.LoadChannels)
    }

    fun onEvent(event: SelectPayoutChannelUiEvent) {
        when (event) {
            SelectPayoutChannelUiEvent.LoadChannels -> loadChannels()
            is SelectPayoutChannelUiEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                applyFilter()
            }
            SelectPayoutChannelUiEvent.OnBackClicked -> { /* Handled in UI */ }
        }
    }

    private fun loadChannels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPayoutChannelsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            channels = result.data,
                            filteredChannels = result.data 
                        ) 
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun applyFilter() {
        val query = _uiState.value.searchQuery
        val filtered = if (query.isBlank()) {
            _uiState.value.channels
        } else {
            _uiState.value.channels.filter { 
                it.channelName.contains(query, ignoreCase = true) || 
                it.channelCode.contains(query, ignoreCase = true) 
            }
        }
        _uiState.update { it.copy(filteredChannels = filtered) }
    }
}
