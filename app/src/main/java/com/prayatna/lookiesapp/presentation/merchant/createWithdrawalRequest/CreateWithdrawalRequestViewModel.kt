package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.withdrawal.CreateWithdrawalRequestInput
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantBankAccountsUseCase
import com.prayatna.lookiesapp.domain.usecase.withdrawal.CreateWithdrawalRequestUseCase
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestEffect
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestEvent
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWithdrawalRequestViewModel @Inject constructor(
    private val createWithdrawalRequestUseCase: CreateWithdrawalRequestUseCase,
    private val getMerchantBankAccountsUseCase: GetMerchantBankAccountsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateWithdrawalRequestUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<CreateWithdrawalRequestEffect>()
    val effect = _effect.receiveAsFlow()

    private val businessId: String = savedStateHandle["businessId"] ?: ""

    init {
        getMerchantBankAccount()
    }

    private fun getMerchantBankAccount() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getMerchantBankAccountsUseCase(businessId)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bankCode = result.data[0].bankCode,
                            accountNumber = result.data[0].accountNumber,
                            accountName = result.data[0].accountHolderName
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: CreateWithdrawalRequestEvent) {
        when (event) {
            is CreateWithdrawalRequestEvent.AccountNameChanged -> _uiState.update { it.copy(accountName = event.accountName) }
            is CreateWithdrawalRequestEvent.AccountNumberChanged -> _uiState.update { it.copy(accountNumber = event.accountNumber) }
            is CreateWithdrawalRequestEvent.AmountChanged -> _uiState.update { it.copy(amount = event.amount) }
            is CreateWithdrawalRequestEvent.BankCodeChanged -> _uiState.update { it.copy(bankCode = event.bankCode) }
            CreateWithdrawalRequestEvent.BackClicked -> {
                viewModelScope.launch { _effect.send(CreateWithdrawalRequestEffect.NavigateBack) }
            }
            CreateWithdrawalRequestEvent.DismissDialog -> {
                _uiState.update { it.copy(errorMessage = null, successMessage = null) }
            }
            CreateWithdrawalRequestEvent.SubmitClicked -> submitRequest()
        }
    }

    private fun submitRequest() {
        val state = _uiState.value
        if (state.amount.isBlank() || state.bankCode.isBlank() || state.accountNumber.isBlank() || state.accountName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        val amountLong = state.amount.toLongOrNull() ?: run {
            _uiState.update { it.copy(errorMessage = "Invalid amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val input = CreateWithdrawalRequestInput(
                merchantId = businessId,
                amount = amountLong,
                bankCode = state.bankCode,
                accountNumber = state.accountNumber,
                accountName = state.accountName
            )
            when (val result = createWithdrawalRequestUseCase(input)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Withdrawal request submitted successfully") }
                    _effect.send(CreateWithdrawalRequestEffect.ShowMessage("Withdrawal request submitted successfully"))
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}
