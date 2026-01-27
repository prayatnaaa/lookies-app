package com.prayatna.lookiesapp.presentation.transaction.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.payment.CreateXenditPaymentUseCase
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentEvent
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val createXenditPaymentUseCase: CreateXenditPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: PaymentEvent) {
        when (event) {

            is PaymentEvent.SelectMethod ->
                _uiState.update { it.copy(selectedMethod = event.method) }

            is PaymentEvent.PhoneChanged ->
                _uiState.update { it.copy(phoneNumber = event.value) }

            is PaymentEvent.CardNumberChanged ->
                _uiState.update { it.copy(cardNumber = event.value) }

            is PaymentEvent.CardExpiryChanged ->
                _uiState.update { it.copy(cardExpiry = event.value) }

            is PaymentEvent.CardCvvChanged ->
                _uiState.update { it.copy(cardCvv = event.value) }

            is PaymentEvent.SubmitPayment ->
                submitPayment(event)
        }
    }

    private fun submitPayment(event: PaymentEvent.SubmitPayment) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = createXenditPaymentUseCase(
                state = _uiState.value,
                orderId = event.orderId,
                merchantId = event.merchantId,
                amount = event.amount
            )

            when (result) {
                is DataResult.Success -> {
                    val redirectUrl =
                        result.data.paymentToken
                            ?.get("actions")
                            ?.jsonArray
                            ?.firstOrNull()
                            ?.jsonObject
                            ?.get("value")
                            ?.jsonPrimitive
                            ?.content

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            paymentSuccessUrl = redirectUrl
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}
