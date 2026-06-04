package com.prayatna.lookiesapp.presentation.partner.partnerRefund

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.refund.GetRefundByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.refund.ProcessRefundUseCase
import com.prayatna.lookiesapp.domain.usecase.refund.UpdateRefundStatusUseCase
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundEffect
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundEvent
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundUiState
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
class PartnerRefundViewModel @Inject constructor(
    private val getRefundByIdUseCase: GetRefundByIdUseCase,
    private val updateRefundStatusUseCase: UpdateRefundStatusUseCase,
    private val processRefundUseCase: ProcessRefundUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val refundId: String = checkNotNull(savedStateHandle["refundId"])

    private val _state = MutableStateFlow(PartnerRefundUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PartnerRefundEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getRefund(refundId)
    }

    fun onEvent(event: PartnerRefundEvent) {
        when (event) {
            PartnerRefundEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.emit(PartnerRefundEffect.NavigateBack)
                }
            }
            is PartnerRefundEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.value) }
            }
            PartnerRefundEvent.UpdateClicked -> updateRefundStatus()
            is PartnerRefundEvent.StatusChanged -> {
                _state.update { it.copy(status = event.value) }
            }

            PartnerRefundEvent.ProcessClicked -> processRefund()
        }
    }

    private fun processRefund() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = processRefundUseCase(refundId)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                    _effect.emit(PartnerRefundEffect.ShowToast(result.error))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(PartnerRefundEffect.ShowToast("Refund processed successfully!"))
                    getRefund(refundId)
                }
                else -> Unit
            }
        }
    }

    private fun updateRefundStatus() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = updateRefundStatusUseCase(
                id = refundId,
                status = state.value.status,
                note = state.value.notes
            )) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                    _effect.emit(PartnerRefundEffect.ShowToast(result.error))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(PartnerRefundEffect.ShowToast("Refund updated successfully!"))
                    getRefund(refundId)
                }
                else -> Unit
            }
        }
    }

    private fun getRefund(id: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getRefundByIdUseCase(id)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                    _effect.emit(PartnerRefundEffect.ShowToast(result.error))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false, data = result.data) }
                    _state.update { it.copy(status = result.data.status) }
                    _state.update { it.copy(notes = result.data.adminNotes) }
                }
                else -> Unit
            }
        }
    }
}
