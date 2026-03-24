package com.prayatna.lookiesapp.presentation.payment.qrPayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.CreateQrisPaymentRequestInput
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateQrisPaymentRequestUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPaymentAttemptUseCase
import com.prayatna.lookiesapp.presentation.payment.qrPayment.state.QrPaymentUiState
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
class QrPaymentViewModel @Inject constructor(
    private val createQrisPaymentRequestUseCase: CreateQrisPaymentRequestUseCase,
    private val getPaymentAttemptUseCase: GetPaymentAttemptUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(QrPaymentUiState())
    val uiState: StateFlow<QrPaymentUiState> = _uiState.asStateFlow()

    fun getPaymentAttempt(orderId: String) {
        viewModelScope.launch {
            getPaymentAttemptUseCase(orderId).collectLatest { result ->
                when (result) {
                    is DataResult.Success -> {
                        if (result.data.status == "paid") {
                            _uiState.update {
                                it.copy(isPaid = true)
                            }
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = result.error)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun createQrisPaymentRequest(
        merchantId: String,
        orderId: String,
        amount: Int,
        description: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val data = CreateQrisPaymentRequestInput(
                merchantId = merchantId,
                orderId = orderId,
                amount = amount,
                description = description ?: "Order for $orderId"
            )
            val result = createQrisPaymentRequestUseCase(data = data)

            _uiState.update {
                when (result) {
                    is DataResult.Success -> {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            qrPaymentData = result.data
                        )
                    }
                    is DataResult.Error -> {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                    else -> it.copy(isLoading = true)
                }
            }
        }
    }
}