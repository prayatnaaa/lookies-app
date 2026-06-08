package com.prayatna.lookiesapp.presentation.admin.event

import androidx.lifecycle.SavedStateHandle
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
class AdminEventViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEventUiState())
    val uiState = _uiState.asStateFlow()

    init {
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

    fun getEvents() {
        val status = _uiState.value.status?.type
        val title = _uiState.value.title.ifBlank { null }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getEventsUseCase(
                title = title,
                status = status
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
//        getEvents()
    }

    fun onStatusSelected(status: EventStatus?) {
        val newStatus = if (_uiState.value.status == status) null else status
        _uiState.update { it.copy(status = newStatus) }
        getEvents()
    }

    fun retry() = getEvents()
}