package com.prayatna.lookiesapp.presentation.admin.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.GetAdminTransactionListUseCase
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiEffect
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiEvent
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTransactionListViewModel @Inject constructor(
    private val getAdminTransactionListUseCase: GetAdminTransactionListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminTransactionListUiState())
    val uiState: StateFlow<AdminTransactionListUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AdminTransactionListUiEffect>()
    val effect: SharedFlow<AdminTransactionListUiEffect> = _effect.asSharedFlow()

    init {
        onEvent(AdminTransactionListUiEvent.LoadTransactions)
    }

    fun onEvent(event: AdminTransactionListUiEvent) {
        when (event) {
            AdminTransactionListUiEvent.LoadTransactions -> loadTransactions()
            AdminTransactionListUiEvent.RefreshTransactions -> refreshTransactions()
            is AdminTransactionListUiEvent.OnStatusFilterChanged -> {
                _uiState.update { it.copy(selectedStatus = event.status, offset = 0) }
                loadTransactions()
            }
            is AdminTransactionListUiEvent.OnTransactionClicked -> {
                viewModelScope.launch {
                    _effect.emit(AdminTransactionListUiEffect.NavigateToDetail(event.orderId))
                }
            }
            AdminTransactionListUiEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(AdminTransactionListUiEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            when (val result = getAdminTransactionListUseCase(
                limit = state.limit,
                offset = state.offset,
                status = state.selectedStatus
            )) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, transactions = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                    _effect.emit(AdminTransactionListUiEffect.ShowError(result.error))
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun refreshTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, offset = 0) }
            val state = _uiState.value
            when (val result = getAdminTransactionListUseCase(
                limit = state.limit,
                offset = 0,
                status = state.selectedStatus
            )) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isRefreshing = false, transactions = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _effect.emit(AdminTransactionListUiEffect.ShowError(result.error))
                }
                else -> _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }
}
