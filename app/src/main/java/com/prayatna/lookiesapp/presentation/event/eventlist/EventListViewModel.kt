package com.prayatna.lookiesapp.presentation.event.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState = _uiState.asStateFlow()

    fun getEvents(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.events.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getEventsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            events = result.data,
                            filteredEvents = result.data,
                            errorMessage = null
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

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterEvents(query)
    }

    private fun filterEvents(query: String) {
        val currentEvents = _uiState.value.events

        if (query.isBlank()) {
            _uiState.update { it.copy(filteredEvents = currentEvents) }
            return
        }

        val filtered = currentEvents.filter { event ->
            event.title.contains(query, ignoreCase = true) ||
                    event.location.contains(query, ignoreCase = true) ||
                    event.organizer.name.contains(query, ignoreCase = true)
        }

        _uiState.update { it.copy(filteredEvents = filtered) }
    }

    fun retry() = getEvents(forceRefresh = true)
}