package com.prayatna.lookiesapp.presentation.admin.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEventUiState())
    val uiState = _uiState.asStateFlow()

    fun getEvents(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.events.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = eventRepository.getEvents()) {
                is DataResult.Success -> _uiState.update {
                    it.copy(isLoading = false, events = result.data, errorMessage = null)
                }
                is DataResult.Error -> _uiState.update {
                    Log.d("EVENT", result.toString())
                    it.copy(isLoading = false, errorMessage = result.error)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun retry() = getEvents(forceRefresh = true)
}