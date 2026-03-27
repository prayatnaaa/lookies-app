package com.prayatna.lookiesapp.presentation.admin.event

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
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEventUiState())
    val uiState = _uiState.asStateFlow()

    fun getEvents(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.events.isNotEmpty()) return
        val status = _uiState.value.status?.type
        val title = _uiState.value.title

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

    fun onStatusSelected(status: EventStatus?) {
        val newStatus = if (_uiState.value.status == status) null else status
        _uiState.update { it.copy(status = newStatus) }
        getEvents(forceRefresh = true)
    }

    fun retry() = getEvents(forceRefresh = true)
}