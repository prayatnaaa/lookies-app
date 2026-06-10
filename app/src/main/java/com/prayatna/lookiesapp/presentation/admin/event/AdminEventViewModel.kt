package com.prayatna.lookiesapp.presentation.admin.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.event.EventFormat
import com.prayatna.lookiesapp.domain.model.event.TEventType
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.event.GetEventsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEventViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val eventRepository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEventUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMeta()
        getEvents()
        observeRefresh()
    }

    private fun observeRefresh() {
        viewModelScope.launch {
            savedStateHandle
                .getStateFlow("refresh", false)
                .collect { shouldRefresh ->
                    if (shouldRefresh) {
                        getEvents()
                        savedStateHandle["refresh"] = false
                    }
                }
        }
    }

    private fun loadMeta() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMeta = true) }
            coroutineScope {
                val typesDeferred = async { eventRepository.getEventTypes() }
                val formatsDeferred = async { eventRepository.getEventFormats() }

                val typesResult = typesDeferred.await()
                val formatsResult = formatsDeferred.await()

                if (typesResult is DataResult.Success && formatsResult is DataResult.Success) {
                    _uiState.update { it.copy(
                        eventTypes = typesResult.data,
                        eventFormats = formatsResult.data,
                        isLoadingMeta = false
                    ) }
                } else {
                    _uiState.update { it.copy(isLoadingMeta = false) }
                }
            }
        }
    }

    fun getEvents() {
        val status = _uiState.value.status?.type
        val title = _uiState.value.title.ifBlank { null }
        val type = _uiState.value.selectedType?.name
        val format = _uiState.value.selectedFormat?.name

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getEventsUseCase(
                title = title,
                status = status,
                eventType = type,
                eventFormat = format
            )) {
                is DataResult.Success -> _uiState.update {
                    it.copy(isLoading = false, events = result.data, errorMessage = null)
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onStatusSelected(status: EventStatus?) {
        val newStatus = if (_uiState.value.status == status) null else status
        _uiState.update { it.copy(status = newStatus) }
        getEvents()
    }

    fun onTypeSelected(type: TEventType?) {
        val newType = if (_uiState.value.selectedType == type) null else type
        _uiState.update { it.copy(selectedType = newType) }
        getEvents()
    }

    fun onFormatSelected(format: EventFormat?) {
        val newFormat = if (_uiState.value.selectedFormat == format) null else format
        _uiState.update { it.copy(selectedFormat = newFormat) }
        getEvents()
    }

    fun retry() = getEvents()
}
