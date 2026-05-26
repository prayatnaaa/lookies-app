package com.prayatna.lookiesapp.presentation.payment.vaPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.CreateVaPaymentRequestInput
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateVaPaymentUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPaymentAttemptUseCase
import com.prayatna.lookiesapp.presentation.payment.vaPayment.state.VaPaymentUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class VaPaymentViewModel @Inject constructor(
    private val createVaPaymentUseCase: CreateVaPaymentUseCase,
    private val getPaymentAttemptUseCase: GetPaymentAttemptUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaPaymentUiState())
    val uiState: StateFlow<VaPaymentUiState> = _uiState.asStateFlow()

    fun getPaymentAttempt(orderId: String) {
        viewModelScope.launch {
            getPaymentAttemptUseCase(orderId).collectLatest { result ->
                when (result) {
                    is DataResult.Success -> {
                        Log.d("VA_PAYMENT", "Status: ${result.data.status}")
                        if (result.data.status == "paid") {
                            _uiState.update { it.copy(isPaid = true) }
                        }
                    }
                    is DataResult.Error -> {
                        _uiState.update { it.copy(errorMessage = result.error) }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun createVaPaymentRequest(
        merchantId: String,
        orderId: String,
        amount: Int,
        channelCode: String,
        customerName: String,
        description: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val expiresAt = Instant.now()
                .plus(5, ChronoUnit.MINUTES)
                .toString()

            val input = CreateVaPaymentRequestInput(
                merchantId = merchantId,
                orderId = orderId,
                amount = amount,
                channelCode = channelCode,
                customerName = customerName,
                expiresAt = expiresAt,
                description = description ?: "VA Payment for $orderId"
            )

            val result = createVaPaymentUseCase(input)

            _uiState.update {
                when (result) {
                    is DataResult.Success -> {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            vaPaymentData = result.data
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
