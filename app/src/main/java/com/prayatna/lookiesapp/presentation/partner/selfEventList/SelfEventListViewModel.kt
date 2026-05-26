package com.prayatna.lookiesapp.presentation.partner.selfEventList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.event.GetEventFormatsUseCase
import com.prayatna.lookiesapp.domain.usecase.event.GetEventTypesUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.GetSelfEventsUseCase
import com.prayatna.lookiesapp.presentation.partner.selfEventList.state.SelfEventListUiEvent
import com.prayatna.lookiesapp.presentation.partner.selfEventList.state.SelfEventListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelfEventListViewModel @Inject constructor(
    private val getSelfEventsUseCase: GetSelfEventsUseCase,
    private val getEventFormatsUseCase: GetEventFormatsUseCase,
    private val getEventTypesUseCase: GetEventTypesUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _uiState = MutableStateFlow(SelfEventListUiState())
    val uiState: StateFlow<SelfEventListUiState> = _uiState.asStateFlow()

    val refresh = savedStateHandle.getStateFlow(
        key = "shouldRefresh",
        initialValue = false
    )

    init {
        loadInitialData()
        loadSelfEvents()
        observeRefresh()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val formatsDeferred = async { getEventFormatsUseCase() }
            val typesDeferred = async { getEventTypesUseCase() }

            val formatsResult = formatsDeferred.await()
            if (formatsResult is DataResult.Success) {
                _uiState.update { it.copy(formats = formatsResult.data) }
            }

            val typesResult = typesDeferred.await()
            if (typesResult is DataResult.Success) {
                _uiState.update { it.copy(types = typesResult.data) }
            }
        }
    }

    private fun observeRefresh() {
        viewModelScope.launch {
            refresh.collect { shouldRefresh ->
                if (shouldRefresh) {
                    loadSelfEvents()

                    savedStateHandle["shouldRefresh"] = false
                }
            }
        }
    }

    private fun loadSelfEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val state = _uiState.value
            val businessId: String = savedStateHandle.get<String>("businessId") ?: ""

            when (val response = getSelfEventsUseCase(
                status = state.selectedStatus,
                name = state.searchQuery.ifBlank { null },
                businessId = businessId,
                eventFormatId = state.selectedFormatId,
                eventTypeId = state.selectedTypeId
            )) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = response.error, isLoading = false)
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(events = response.data, isLoading = false)
                    }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: SelfEventListUiEvent) {
        when (event) {
            is SelfEventListUiEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            SelfEventListUiEvent.OnSearchTriggered -> {
                loadSelfEvents()
            }
            is SelfEventListUiEvent.OnStatusSelected -> {
                val newStatus = if (_uiState.value.selectedStatus == event.status) null else event.status
                _uiState.update { it.copy(selectedStatus = newStatus) }
                loadSelfEvents()
            }
            is SelfEventListUiEvent.OnFormatSelected -> {
                val newFormat = if (_uiState.value.selectedFormatId == event.id) null else event.id
                _uiState.update { it.copy(selectedFormatId = newFormat) }
            }
            is SelfEventListUiEvent.OnTypeSelected -> {
                val newType = if (_uiState.value.selectedTypeId == event.id) null else event.id
                _uiState.update { it.copy(selectedTypeId = newType) }
            }
            SelfEventListUiEvent.OnFilterSheetToggle -> {
                _uiState.update { it.copy(isFilterSheetOpen = !it.isFilterSheetOpen) }
            }
            SelfEventListUiEvent.OnApplyFilters -> {
                _uiState.update { it.copy(isFilterSheetOpen = false) }
                loadSelfEvents()
            }
            SelfEventListUiEvent.OnResetFilters -> {
                _uiState.update {
                    it.copy(
                        selectedStatus = null,
                        selectedFormatId = null,
                        selectedTypeId = null,
                        isFilterSheetOpen = false
                    )
                }
                loadSelfEvents()
            }
            SelfEventListUiEvent.OnRetry -> loadSelfEvents()
        }
    }

    // Deprecated helpers
    fun onSearchQueryChange(newQuery: String) = onEvent(SelfEventListUiEvent.OnSearchQueryChange(newQuery))
    fun onSearchTriggered() = onEvent(SelfEventListUiEvent.OnSearchTriggered)
    fun onFilterSelected(status: String?) = onEvent(SelfEventListUiEvent.OnStatusSelected(status))
    fun retry() = onEvent(SelfEventListUiEvent.OnRetry)
}
