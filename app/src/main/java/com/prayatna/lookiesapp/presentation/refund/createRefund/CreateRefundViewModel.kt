package com.prayatna.lookiesapp.presentation.refund.createRefund

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.transaction.CreateRefundRequestInput
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateRefundRequestUseCase
import com.prayatna.lookiesapp.presentation.refund.createRefund.state.CreateRefundEvent
import com.prayatna.lookiesapp.presentation.refund.createRefund.state.CreateRefundFormState
import com.prayatna.lookiesapp.presentation.refund.createRefund.state.CreateRefundUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRefundViewModel @Inject constructor(
    private val createRefundRequestUseCase: CreateRefundRequestUseCase,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle["orderId"] ?: ""
    private val totalAmount: Float= savedStateHandle["totalAmount"] ?: 0f

    private val _uiState = MutableStateFlow<CreateRefundUiState>(CreateRefundUiState.Idle)
    val uiState: StateFlow<CreateRefundUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CreateRefundFormState(orderId = orderId, amount = totalAmount.toString()))
    val formState: StateFlow<CreateRefundFormState> = _formState.asStateFlow()

    fun onEvent(event: CreateRefundEvent) {
        when (event) {
            is CreateRefundEvent.OrderIdChanged -> _formState.update { it.copy(orderId = event.value) }
            is CreateRefundEvent.AmountChanged -> _formState.update { it.copy(amount = event.value) }
            is CreateRefundEvent.BankCodeChanged -> _formState.update { it.copy(bankCode = event.value) }
            is CreateRefundEvent.AccountNumberChanged -> _formState.update { it.copy(accountNumber = event.value) }
            is CreateRefundEvent.AccountHolderNameChanged -> _formState.update { it.copy(accountHolderName = event.value) }
            is CreateRefundEvent.ReasonChanged -> _formState.update { it.copy(reason = event.value) }
            is CreateRefundEvent.ProofImageSelected -> _formState.update { it.copy(proofImageUri = event.uri) }
            is CreateRefundEvent.Submit -> submitRefund()
            is CreateRefundEvent.DismissError -> _uiState.value = CreateRefundUiState.Idle
            is CreateRefundEvent.OnBackClick -> { /* handled in Route */ }
        }
    }

    private fun submitRefund() {
        val form = _formState.value

        if (form.proofImageUri == null) {
            _uiState.value = CreateRefundUiState.Error("Proof image is required")
            return
        }

        _uiState.value = CreateRefundUiState.Loading

        viewModelScope.launch {
            val proofImageBytes = readUriBytes(form.proofImageUri)

            val input = CreateRefundRequestInput(
                orderId = form.orderId,
                userId = "",
                amount = form.amount,
                bankCode = form.bankCode,
                accountNumber = form.accountNumber,
                accountHolderName = form.accountHolderName,
                reason = form.reason
            )

            when (val result = createRefundRequestUseCase(input, proofImageBytes)) {
                is DataResult.Success -> {
                    _uiState.value = CreateRefundUiState.Success(result.data)
                }
                is DataResult.Error -> {
                    _uiState.value = CreateRefundUiState.Error(result.error)
                }
                else -> Unit
            }
        }
    }

    private fun readUriBytes(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            null
        }
    }
}
