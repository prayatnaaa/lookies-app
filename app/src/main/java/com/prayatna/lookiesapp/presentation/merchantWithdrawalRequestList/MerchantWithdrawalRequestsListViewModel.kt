package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.withdrawal.GetWithdrawalRequestsByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEffect
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEffect.*
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEvent
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListUiState
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
class MerchantWithdrawalRequestsListViewModel @Inject constructor(
    private val getWithdrawalRequestsByMerchantIdUseCase: GetWithdrawalRequestsByMerchantIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val businessId: String = savedStateHandle["businessId"] ?: ""

    private val _state = MutableStateFlow(MerchantWithdrawalRequestListUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MerchantWithdrawalRequestListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        getMerchantWithdrawalRequest()
    }

    fun onEvent(event: MerchantWithdrawalRequestListEvent) {
        when(event) {
            MerchantWithdrawalRequestListEvent.BackClicked -> {
                viewModelScope.launch { _effect.send(NavigateBack) }
            }
            is MerchantWithdrawalRequestListEvent.DetailClicked -> {
                viewModelScope.launch { _effect.send(NavigateToDetail(event.id)) }
            }

            MerchantWithdrawalRequestListEvent.CreateWithdrawalClicked -> {
                viewModelScope.launch {
                    _effect.send(NavigateCreateWithdrawal(businessId))
                }
            }
        }
    }

    private fun getMerchantWithdrawalRequest() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getWithdrawalRequestsByMerchantIdUseCase(businessId)) {
                is DataResult.Error -> {
                    _state.update {
                        it.copy(isLoading = false, errorMessage = result.error)
                    }
                }
                is DataResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            withdrawalRequests = result.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}
