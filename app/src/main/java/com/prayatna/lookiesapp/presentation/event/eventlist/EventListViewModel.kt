package com.prayatna.lookiesapp.presentation.event.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val currentQuery = _uiState.value.searchQuery.ifBlank { null }
            val currentStatus = _uiState.value.selectedStatus
            val ascending = _uiState.value.isTicketPriceAscending

            when (val result = getEventsUseCase(
                title = currentQuery,
                status = currentStatus,
                isTicketPriceAscending = ascending
            )) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
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

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSearchTriggered() {
        loadEvents()
    }

    fun onFilterSelected(status: String?) {
        val newStatus = if (_uiState.value.selectedStatus == status) null else status
        _uiState.update { it.copy(selectedStatus = newStatus) }
        loadEvents()
    }

    fun onSortToggled() {
        _uiState.update { it.copy(isTicketPriceAscending = !it.isTicketPriceAscending) }
        loadEvents()
    }

    fun retry() {
        loadEvents()
    }
}