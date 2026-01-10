package com.prayatna.lookiesapp.presentation.main.transactionList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetUserTransactionsUseCase
import com.prayatna.lookiesapp.presentation.main.transactionList.state.TransactionListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getUserTransactionsUseCase: GetUserTransactionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionListUiState>(TransactionListUiState.Loading)
    val state = _state.asStateFlow()

    init {
        getTransactions()
    }

    fun getTransactions() {
        viewModelScope.launch {
            _state.value = TransactionListUiState.Loading
            when(val result = getUserTransactionsUseCase()) {
                is DataResult.Error -> {
                    _state.value = TransactionListUiState.Error(
                        message = result.error
                    )
                }

                is DataResult.Success -> {
                    val transactions = result.data
                    Log.d("TransactionListViewModel", "getTransactions: $transactions")

                    if (transactions.isEmpty()) {
                        _state.value = TransactionListUiState.Empty
                    } else {
                        _state.value = TransactionListUiState.Success(transactions)
                    }
                }

                else -> Unit
            }
        }
    }
}