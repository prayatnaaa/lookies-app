package com.prayatna.lookiesapp.presentation.admin.withdrawal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.withdrawal.GetMerchantWithdrawalRequestsUseCase
import com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state.AdminWithdrawalListUiEvent
import com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state.AdminWithdrawalListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminWithdrawalListViewModel @Inject constructor(
    private val getMerchantWithdrawalRequestsUseCase: GetMerchantWithdrawalRequestsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminWithdrawalListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(AdminWithdrawalListUiEvent.LoadWithdrawalRequests)
    }

    fun onEvent(event: AdminWithdrawalListUiEvent) {
        when (event) {
            AdminWithdrawalListUiEvent.LoadWithdrawalRequests -> loadRequests()
            AdminWithdrawalListUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun loadRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getMerchantWithdrawalRequestsUseCase()) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(withdrawalRequests = result.data, isLoading = false) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.error, isLoading = false) }
                }
                else -> {}
            }
        }
    }
}
