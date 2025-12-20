package com.prayatna.lookiesapp.presentation.event.detailevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.usecase.auth.GetRoleUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val getRoleUseCase: GetRoleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailEventUiState())
    val state = _state.asStateFlow()

    private val _quantityValue = MutableStateFlow(0)
    val quantityValue = _quantityValue.asStateFlow()

    private val _roleState = MutableStateFlow("")
    val roleState = _roleState.asStateFlow()

    init {
        viewModelScope.launch {
            getRoleUseCase().collect { role ->
                _roleState.value = role
            }
        }
    }

    fun setQuantityValue(value: Int) {
        _quantityValue.value = value
    }

    fun getEvent(eventId: String, forceRefresh: Boolean = false) {
        if (!forceRefresh && _state.value.info != null) return

        viewModelScope.launch {
            _state.value = DetailEventUiState(isLoading = true)

            when (val result = repository.getEvent(eventId)) {
                is DataResult.Error -> _state.value = DetailEventUiState(errorMessage = result.error)
                is DataResult.Loading -> _state.value = DetailEventUiState(isLoading = true)
                is DataResult.Success -> {
                    _state.value = DetailEventUiState(info = result.data)
                }
                else -> {}
            }
        }
    }

    fun retry(eventId: String) = getEvent(eventId = eventId, forceRefresh = true)
}
