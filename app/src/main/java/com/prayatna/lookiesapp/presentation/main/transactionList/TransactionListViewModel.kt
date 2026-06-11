package com.prayatna.lookiesapp.presentation.main.transactionList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetUserTransactionsUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetProfileUseCase
import com.prayatna.lookiesapp.presentation.main.transactionList.state.TransactionListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getUserTransactionsUseCase: GetUserTransactionsUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionListUiState>(TransactionListUiState.Loading)
    val state = _state.asStateFlow()

    private var customerName: String = "Customer"

    init {
        loadData()
    }

    fun refresh() {
        when (val current = _state.value) {
            is TransactionListUiState.Success -> {
                _state.value = current.copy(isRefreshing = true)
            }

            is TransactionListUiState.Empty -> {
                _state.value = current.copy(isRefreshing = true)
            }

            is TransactionListUiState.Error -> {
                _state.value = TransactionListUiState.Empty(isRefreshing = true)
            }

            else -> Unit
        }

        getTransactions(isRefresh = true)
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = TransactionListUiState.Loading
            
            // Fetch profile first
            getProfileUseCase().collect { profileResult ->
                if (profileResult is DataResult.Success) {
                    customerName = profileResult.data.fullName ?: "Customer"
                }
            }
        }
        getTransactions()
    }

    fun getTransactions(
        isRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            if (!isRefresh) {
                _state.value = TransactionListUiState.Loading
            }

            when (val result = getUserTransactionsUseCase()) {

                is DataResult.Error -> {
                    _state.value = TransactionListUiState.Error(result.error)
                }

                is DataResult.Success -> {
                    if (result.data.isEmpty()) {
                        _state.value = TransactionListUiState.Empty(isRefreshing = false)
                    } else {
                        _state.value = TransactionListUiState.Success(
                            data = result.data,
                            customerName = customerName,
                            isRefreshing = false
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}