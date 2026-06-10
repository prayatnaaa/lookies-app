package com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.ProcessPayoutUseCase
import com.prayatna.lookiesapp.domain.usecase.withdrawal.GetWithdrawalRequestByIdUseCase
import com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation.state.WithdrawalConfirmationEffect
import com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation.state.WithdrawalConfirmationEvent
import com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation.state.WithdrawalConfirmationUiState
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
class WithdrawalConfirmationViewModel @Inject constructor(
    private val getWithdrawalRequestByIdUseCase: GetWithdrawalRequestByIdUseCase,
    private val processPayoutUseCase: ProcessPayoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalConfirmationUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<WithdrawalConfirmationEffect>()
    val effect = _effect.receiveAsFlow()

    private val withdrawalId: String = savedStateHandle["withdrawalId"] ?: ""

    init {
        loadDetails()
    }

    private fun loadDetails() {
        if (withdrawalId.isBlank()) return
        
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getWithdrawalRequestByIdUseCase(withdrawalId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, withdrawalRequest = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: WithdrawalConfirmationEvent) {
        when (event) {
            WithdrawalConfirmationEvent.ProcessPayout -> processPayout()
            WithdrawalConfirmationEvent.BackClicked -> {
                viewModelScope.launch { _effect.send(WithdrawalConfirmationEffect.NavigateBack) }
            }
            is WithdrawalConfirmationEvent.LoadDetails -> loadDetails()
        }
    }

    private fun processPayout() {
        if (withdrawalId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            when (val result = processPayoutUseCase(withdrawalId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isProcessing = false) }
                    _effect.send(WithdrawalConfirmationEffect.ShowMessage("Payout processed successfully"))
                    _effect.send(WithdrawalConfirmationEffect.NavigateToDashboard)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isProcessing = false) }
                    _effect.send(
                        WithdrawalConfirmationEffect.ShowMessage(
                            result.error
                        )
                    )
                    loadDetails()
                }
                else -> Unit
            }
        }
    }
}
