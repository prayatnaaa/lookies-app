package com.prayatna.lookiesapp.presentation.transaction.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.dto.request.payment.*
import com.prayatna.lookiesapp.domain.usecase.payment.CreateXenditPaymentUseCase
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod
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

    fun onMethodSelected(method: PaymentMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun onPhoneNumberChange(value: String) = _uiState.update { it.copy(phoneNumber = value) }
    fun onCardNumberChange(value: String) = _uiState.update { it.copy(cardNumber = value) }
    fun onExpiryChange(value: String) = _uiState.update { it.copy(cardExpiry = value) }
    fun onCvvChange(value: String) = _uiState.update { it.copy(cardCvv = value) }

    fun processPayment(orderId: String, merchantId: String, amount: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val referenceId = "TRX-${System.currentTimeMillis()}"
            val currentState = _uiState.value

            val result = when (currentState.selectedMethod) {
                PaymentMethod.GOPAY -> {
                    val props = GopayChannelProperties(
                        accountMobileNumber = currentState.phoneNumber.ifBlank { "+62810000000" }
                    )
                    val request = CreateXenditPaymentRequest(
                        merchantId = merchantId,
                        orderId = orderId,
                        referenceId = referenceId,
                        requestAmount = amount,
                        channelCode = "GOPAY_RECURRING",
                        channelProperties = props,
                    )
                    createXenditPaymentUseCase(request)
                }
                PaymentMethod.CREDIT_CARD -> {
                    val (month, year) = if (currentState.cardExpiry.contains("/")) {
                        currentState.cardExpiry.split("/")
                    } else listOf("01", "2030")

                    val props = CardChannelProperties(
                        skipThreeDs = false,
                        cardDetails = CardDetails(
                            cardNumber = currentState.cardNumber,
                            expiryMonth = month,
                            expiryYear = "20$year",
                            cardholderFirstName = "Customer",
                            cardholderLastName = "Name",
                            cardholderEmail = "customer@example.com"
                        )
                    )
                    val request = CreateXenditPaymentRequest(
                        merchantId = merchantId,
                        orderId = orderId,
                        referenceId = referenceId,
                        requestAmount = amount,
                        channelCode = "CARDS",
                        channelProperties = props
                    )
                    createXenditPaymentUseCase(request)
                }
            }

            when (result) {
                is DataResult.Success -> {
                    val actions = result.data.paymentToken?.get("actions")?.jsonArray
                    val redirectUrl = actions?.firstOrNull()?.jsonObject?.get("url")?.jsonPrimitive?.content

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            paymentSuccessUrl = redirectUrl,
                            isSuccess = true
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}