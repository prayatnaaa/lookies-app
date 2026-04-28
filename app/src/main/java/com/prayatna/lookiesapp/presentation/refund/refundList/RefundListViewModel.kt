package com.prayatna.lookiesapp.presentation.refund.refundList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.refund.GetRefundsUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.SetRefundAsCompleteUseCase
import com.prayatna.lookiesapp.presentation.refund.refundList.state.RefundListEvent
import com.prayatna.lookiesapp.presentation.refund.refundList.state.RefundListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefundListViewModel @Inject constructor(
    private val getRefundsUseCase: GetRefundsUseCase,
    private val setRefundAsCompleteUseCase: SetRefundAsCompleteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RefundListUiState())
    val uiState: StateFlow<RefundListUiState> = _uiState.asStateFlow()

    init {
        loadRefunds()
    }

    fun loadRefunds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getRefundsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, refunds = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: RefundListEvent) {
        when (event) {
            is RefundListEvent.SetRefundComplete -> setComplete(event.refundId)
            is RefundListEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            is RefundListEvent.DismissSuccess -> _uiState.update { it.copy(successMessage = null) }
            is RefundListEvent.OnBackClick -> { /* handled in Route */ }
        }
    }

    private fun setComplete(refundId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            when (val result = setRefundAsCompleteUseCase(refundId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isProcessing = false, successMessage = "Refund marked as complete") }
                    loadRefunds()
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isProcessing = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}
