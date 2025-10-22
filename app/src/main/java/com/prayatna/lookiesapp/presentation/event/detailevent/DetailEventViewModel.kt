package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailEventUiState())
    val state = _state.asStateFlow()

    fun getEvent(eventId: String, forceRefresh: Boolean = false) {
        if (!forceRefresh && _state.value.info != null) return

        viewModelScope.launch {
            _state.value = DetailEventUiState(isLoading = true)

            when (val result = repository.getEvent(eventId)) {
                is DataResult.Error -> _state.value = DetailEventUiState(errorMessage = result.error)
                is DataResult.Loading -> _state.value = DetailEventUiState(isLoading = true)
                is DataResult.Success -> _state.value = DetailEventUiState(info = result.data)
                else -> {}
            }
        }
    }

    fun retry(eventId: String) = getEvent(eventId = eventId, forceRefresh = true)
}
