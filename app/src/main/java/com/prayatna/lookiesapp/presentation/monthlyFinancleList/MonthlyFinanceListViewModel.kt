package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantProfileUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetMerchantBalanceLogsUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetOrderSplitsByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEffect
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEvent
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyFinanceListViewModel @Inject constructor(
    private val getMerchantProfileUseCase: GetMerchantProfileUseCase,
    private val getOrderSplitsByMerchantIdUseCase: GetOrderSplitsByMerchantIdUseCase,
    private val getMerchantBalanceLogsUseCase: GetMerchantBalanceLogsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val businessId = checkNotNull(savedStateHandle.get<String>("businessId"))
    private var accountId: String? = null

    private val _state = MutableStateFlow(MonthlyFinanceUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MonthlyFinanceEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun onEvent(event: MonthlyFinanceEvent) {
        when (event) {
            MonthlyFinanceEvent.LoadData -> {
                loadData()
            }
            MonthlyFinanceEvent.NavigateBack -> {
                viewModelScope.launch {
                    _effect.send(MonthlyFinanceEffect.NavigateBack)
                }
            }
            is MonthlyFinanceEvent.TabSelected -> {
                _state.update { it.copy(selectedTab = event.index) }
            }
            MonthlyFinanceEvent.WithdrawalListClicked -> {
                val id = accountId ?: return
                viewModelScope.launch {
                    _effect.send(MonthlyFinanceEffect.NavigateToWithdrawalList(id))
                }
            }
            is MonthlyFinanceEvent.PayoutLogClicked -> {
                viewModelScope.launch {
                    _effect.send(MonthlyFinanceEffect.NavigateToOrderDetail(event.orderId))
                }
            }
        }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val profileResult = getMerchantProfileUseCase(businessId)) {
                is DataResult.Success -> {
                    val accountId = profileResult.data.accountId
                    this@MonthlyFinanceListViewModel.accountId = accountId
                    
                    val orderSplitsDeferred = async { getOrderSplitsByMerchantIdUseCase(accountId) }
                    val balanceLogsDeferred = async { getMerchantBalanceLogsUseCase(accountId) }

                    val orderSplitsResult = orderSplitsDeferred.await()
                    val balanceLogsResult = balanceLogsDeferred.await()

                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            orderSplits = if (orderSplitsResult is DataResult.Success) orderSplitsResult.data else emptyList(),
                            balanceLogs = if (balanceLogsResult is DataResult.Success) balanceLogsResult.data else emptyList(),
                            errorMessage = if (orderSplitsResult is DataResult.Error) orderSplitsResult.error else if (balanceLogsResult is DataResult.Error) balanceLogsResult.error else null
                        )
                    }
                }
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = profileResult.error) }
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
