package com.prayatna.lookiesapp.presentation.refund.refundDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.refund.GetRefundByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.refund.UpdateReturnTrackingNumberUseCase
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailEffect
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailEvent
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefundDetailViewModel @Inject constructor(
    private val getRefundByIdUseCase: GetRefundByIdUseCase,
    private val updateReturnTrackingNumberUseCase: UpdateReturnTrackingNumberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val refundId: String = savedStateHandle["refundId"] ?: ""

    private val _uiState = MutableStateFlow(RefundDetailUiState())
    val uiState: StateFlow<RefundDetailUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RefundDetailEffect>()
    val effect: SharedFlow<RefundDetailEffect> = _effect.asSharedFlow()

    init {
        if (refundId.isNotEmpty()) {
            onEvent(RefundDetailEvent.LoadRefund(refundId))
        }
    }

    fun onEvent(event: RefundDetailEvent) {
        when (event) {
            is RefundDetailEvent.LoadRefund -> loadRefund(event.id)
            is RefundDetailEvent.OnTrackingNumberChange -> {
                _uiState.update { it.copy(trackingNumber = event.value) }
            }
            RefundDetailEvent.SubmitTrackingNumber -> submitTrackingNumber()
            RefundDetailEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun loadRefund(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getRefundByIdUseCase(id)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, refund = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun submitTrackingNumber() {
        val tracking = _uiState.value.trackingNumber
        if (tracking.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingTracking = true, errorMessage = null) }
            when (val result = updateReturnTrackingNumberUseCase(refundId, tracking)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSubmittingTracking = false, refund = result.data) }
                    _effect.emit(RefundDetailEffect.ShowToast("Tracking number updated!"))
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSubmittingTracking = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}
