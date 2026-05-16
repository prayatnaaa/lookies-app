package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.UpdateWithdrawalStatusUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.ProcessPayoutUseCase
import com.prayatna.lookiesapp.domain.usecase.withdrawal.GetWithdrawalRequestByIdUseCase
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiEffect
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiEvent
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminWithdrawalDetailViewModel @Inject constructor(
    private val getWithdrawalRequestByIdUseCase: GetWithdrawalRequestByIdUseCase,
    private val updateWithdrawalStatusUseCase: UpdateWithdrawalStatusUseCase,
    private val processPayoutUseCase: ProcessPayoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminWithdrawalDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AdminWithdrawalDetailUiEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: AdminWithdrawalDetailUiEvent) {
        when (event) {
            is AdminWithdrawalDetailUiEvent.LoadDetail -> loadDetail(event.id)
            is AdminWithdrawalDetailUiEvent.UpdateStatus -> updateStatus(event.id, event.status, event.notes)
            is AdminWithdrawalDetailUiEvent.ProcessPayout -> processPayout(event.id)
            AdminWithdrawalDetailUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getWithdrawalRequestByIdUseCase(id)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(withdrawalRequest = result.data, isLoading = false) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                }
                else -> {}
            }
        }
    }

    private fun updateStatus(id: String, status: String, notes: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = updateWithdrawalStatusUseCase(id, status, notes)) {
                is DataResult.Success -> {
                    _effect.emit(AdminWithdrawalDetailUiEffect.ShowToast("Request $status successfully"))
                    _uiState.update { it.copy(withdrawalRequest = result.data, isLoading = false) }
                    _effect.emit(AdminWithdrawalDetailUiEffect.NavigateBack)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                }
                else -> {}
            }
        }
    }

    private fun processPayout(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = processPayoutUseCase(id)) {
                is DataResult.Success -> {
                    _effect.emit(AdminWithdrawalDetailUiEffect.ShowToast("Payout processed successfully"))
                    _uiState.update { it.copy(payoutSuccessData = result.data, isLoading = false) }
                    _effect.emit(AdminWithdrawalDetailUiEffect.NavigateBack)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                }
                else -> {}
            }
        }
    }
}
