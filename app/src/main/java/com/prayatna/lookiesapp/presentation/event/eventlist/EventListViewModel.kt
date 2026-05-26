package com.prayatna.lookiesapp.presentation.event.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListEffect
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListEvent
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    private val _effect = Channel<EventListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            
            when (val result = getEventsUseCase(
                title = state.searchQuery.ifBlank { null },
                status = state.selectedStatus,
                location = state.selectedLocation?.ifBlank { null },
                startDate = state.startDate,
                endDate = state.endDate,
                isTicketPriceAscending = state.isTicketPriceAscending
            )) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            events = result.data,
                            errorMessage = null
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error)
                    }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: EventListEvent) {
        when (event) {
            is EventListEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            EventListEvent.OnSearchTriggered -> {
                loadEvents()
            }
            is EventListEvent.OnStatusSelected -> {
                val newStatus = if (_uiState.value.selectedStatus == event.status) null else event.status
                _uiState.update { it.copy(selectedStatus = newStatus) }
                loadEvents()
            }
            is EventListEvent.OnLocationChange -> {
                _uiState.update { it.copy(selectedLocation = event.location) }
            }
            is EventListEvent.OnStartDateChange -> {
                _uiState.update { it.copy(startDate = event.date) }
            }
            is EventListEvent.OnEndDateChange -> {
                _uiState.update { it.copy(endDate = event.date) }
            }
            EventListEvent.OnSortToggled -> {
                _uiState.update { it.copy(isTicketPriceAscending = !it.isTicketPriceAscending) }
                loadEvents()
            }
            EventListEvent.OnFilterSheetToggle -> {
                _uiState.update { it.copy(isFilterSheetOpen = !it.isFilterSheetOpen) }
            }
            EventListEvent.OnApplyFilters -> {
                _uiState.update { it.copy(isFilterSheetOpen = false) }
                loadEvents()
            }
            EventListEvent.OnResetFilters -> {
                _uiState.update { 
                    it.copy(
                        selectedStatus = null,
                        selectedLocation = null,
                        startDate = null,
                        endDate = null,
                        isFilterSheetOpen = false
                    ) 
                }
                loadEvents()
            }
            EventListEvent.OnRetry -> loadEvents()
            EventListEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.send(EventListEffect.NavigateBack)
                }
            }
            is EventListEvent.OnDetailEventClicked -> {
                viewModelScope.launch {
                    _effect.send(EventListEffect.NavigateToDetail(event.id))
                }
            }
        }
    }

    fun retry() = onEvent(EventListEvent.OnRetry)
}
