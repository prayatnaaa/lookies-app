package com.prayatna.lookiesapp.presentation.admin.detailEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.ApproveEventUseCase
import com.prayatna.lookiesapp.domain.usecase.admin.RejectEventUseCase
import com.prayatna.lookiesapp.domain.usecase.event.GetDetailEventUseCase
import com.prayatna.lookiesapp.domain.usecase.event.GetRevenueRulesByEventIdUseCase
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEffect
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEvent
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailEventViewModel @Inject constructor(
    private val getDetailEventUseCase: GetDetailEventUseCase,
    private val getRevenueRulesByEventIdUseCase: GetRevenueRulesByEventIdUseCase,
    private val approveEventUseCase: ApproveEventUseCase,
    private val rejectEventUseCase: RejectEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDetailEventUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AdminDetailEventUiEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: AdminDetailEventUiEvent) {
        when (event) {
            is AdminDetailEventUiEvent.LoadDetail -> loadDetail(event.eventId)
            is AdminDetailEventUiEvent.ApproveEvent -> approveEvent(event.eventId)
            is AdminDetailEventUiEvent.RejectEvent -> rejectEvent(event.eventId, event.reason)
            AdminDetailEventUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadDetail(eventId: String) {
        viewModelScope.launch {
            getDetailEventUseCase(eventId).collect { result ->
                when (result) {
                    is DataResult.Success -> {
                        _uiState.update { it.copy(event = result.data, isLoading = false) }
                        result.data.id.toIntOrNull()?.let { id ->
                            loadRevenueRules(id)
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                    }
                    is DataResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun loadRevenueRules(eventId: Int) {
        viewModelScope.launch {
            when (val result = getRevenueRulesByEventIdUseCase(eventId)) {
                is DataResult.Success -> {
                    Log.d("Revenue", result.data.toString())
                    _uiState.update { it.copy(revenueRules = result.data) }
                }
                else -> {}
            }
        }
    }

    private fun approveEvent(eventId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeciding = true) }
            when (val result = approveEventUseCase(eventId)) {
                is DataResult.Success -> {
                    _effect.emit(AdminDetailEventUiEffect.ShowToast("Event approved successfully"))
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isDeciding = false) }
                }
                else -> {}
            }
        }
    }

    private fun rejectEvent(eventId: Int, reason: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeciding = true) }
            when (val result = rejectEventUseCase(eventId, reason)) {
                is DataResult.Success -> {
                    _effect.emit(AdminDetailEventUiEffect.ShowToast("Event rejected successfully"))
//                    _effect.emit(AdminDetailEventUiEffect.NavigateBack)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isDeciding = false) }
                }
                else -> {}
            }
        }
    }
}
