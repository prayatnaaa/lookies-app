package com.prayatna.lookiesapp.presentation.refund.orderRefunds

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.domain.usecase.transaction.GetRefundsByOrderIdUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderRefundsUiState(
    val isLoading: Boolean = false,
    val refunds: List<Refund> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class OrderRefundsViewModel @Inject constructor(
    private val getRefundsByOrderIdUseCase: GetRefundsByOrderIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle["orderId"] ?: ""

    private val _uiState = MutableStateFlow(OrderRefundsUiState())
    val uiState: StateFlow<OrderRefundsUiState> = _uiState.asStateFlow()

    init {
        loadRefunds()
    }

    fun loadRefunds() {
        if (orderId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getRefundsByOrderIdUseCase(orderId)) {
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
}
