package com.prayatna.lookiesapp.presentation.payment.existingVaPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPaymentAttemptByOrderIdUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPaymentAttemptUseCase
import com.prayatna.lookiesapp.presentation.payment.existingVaPayment.state.ExistingVaPaymentUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExistingVaPaymentViewModel @Inject constructor(
    private val getPaymentAttemptByOrderIdUseCase: GetPaymentAttemptByOrderIdUseCase,
    private val getPaymentAttemptUseCase: GetPaymentAttemptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExistingVaPaymentUiState())
    val uiState: StateFlow<ExistingVaPaymentUiState> = _uiState.asStateFlow()

    fun getExistingPaymentAttempt(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val result = getPaymentAttemptByOrderIdUseCase(orderId)) {
                is DataResult.Success -> {
                    val attempt = result.data
                    if (attempt != null) {
                        _uiState.update { it.copy(isLoading = false, paymentAttempt = attempt) }
                        if (attempt.status != "paid") {
                            observePaymentStatus(orderId)
                        } else {
                            _uiState.update { it.copy(isPaid = true) }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Payment information not found.") }
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun observePaymentStatus(orderId: String) {
        viewModelScope.launch {
            getPaymentAttemptUseCase(orderId).collectLatest { result ->
                if (result is DataResult.Success && result.data.status == "paid") {
                    _uiState.update { it.copy(isPaid = true) }
                }
            }
        }
    }
}
